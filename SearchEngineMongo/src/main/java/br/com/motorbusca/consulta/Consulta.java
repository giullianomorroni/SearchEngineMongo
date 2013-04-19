package br.com.motorbusca.consulta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import br.com.motorbusca.base.BaseDeDados;
import br.com.motorbusca.modelo.PontoDeInteresse;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Consulta {

	private final Integer CIRCUNFERENCIA_TERRA = 6371;
	private final Integer METROS = 1000;

	/**
	 * Consulta pelos pontos próximos a um determinado ponto, respeitando o raio de distância.
	 * Retorna a distância em metros.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param raioDeBusca
	 * @param paginaAtual
	 * @param totalPorPagina
	 * @return
	 */
	public List<PontoDeInteresse> pontosPorProximidade(Double latitude, Double longitude, Double raioDeBusca, Integer paginaAtual, Integer totalPorPagina) {
		List<PontoDeInteresse> pontos = new ArrayList<>();

		BasicDBObject query = new BasicDBObject(); 
		query.append("geoNear", "poi"); 

		Double[] posicao = {longitude, latitude};

		//A paǵina 1 vem como 0, para resovler sempre adicionar mais 1.
		paginaAtual += 1;
		int totalRegistros = paginaAtual * totalPorPagina;

		query.append("near", posicao);
		query.append("spherical", true);
		query.append("maxDistance", raioDeBusca / CIRCUNFERENCIA_TERRA); 
		//multiplicado por 1.000 para converter de Km para m
		query.append("distanceMultiplier", CIRCUNFERENCIA_TERRA * METROS);
		query.append("sort", "dis");
		query.append("num", totalRegistros);

		CommandResult resultado = BaseDeDados.conexao().command(query);
		BasicDBObject json = (BasicDBObject) JSON.parse(resultado.toString());
		BasicDBList resultados = (BasicDBList) json.get("results");
		if(resultados == null)
			return pontos;

		for (Object obj : resultados) {
			BasicDBObject registro = (BasicDBObject) obj;

			BasicDBObject poi = (BasicDBObject) registro.get("obj");
			PontoDeInteresse ponto = new PontoDeInteresse();
			ponto.setIdCorporativo((Long) poi.get("idCorporativo"));
			ponto.setNome((String) poi.get("nome"));
			ponto.setCategoria((String) poi.get("categoria"));
			ponto.setSubCategoria((String) poi.get("subCategoria"));
			ponto.setLogradouro((String) poi.get("lograduro"));
			ponto.setPictograma((String) poi.get("pictograma"));
			ponto.setPossuiOferta((Boolean) poi.get("possuiOferta"));
			ponto.setTotalPessoasPresentes((Integer) poi.get("totalPessoasPresentes"));
			BasicDBList posicionamento = (BasicDBList) poi.get("posicao");
			ponto.setLongitude((Double) posicionamento.get(0));
			ponto.setLatitude((Double) posicionamento.get(1));

			Double distancia = registro.getDouble("dis");
			BigDecimal d = new BigDecimal(distancia);
			d = d.setScale(0, RoundingMode.HALF_UP);
			ponto.setDistancia(d.doubleValue());

			pontos.add(ponto);
		}
		return pontos;
	}

	/**
	 * Consulta pelos pontos próximos a um determinado ponto, respeitando o raio de distância.
	 * Retorna a distância em metros. A palavra chave é converitda em regex e usada contra os campos
	 * nome, categoria e subcategoria.
	 * 
	 * Se passar um filtro nulo, utiliza um sistema de busca mais rápido com o geoNear (nativo do mongodb)
	 * 
	 * @param latitude
	 * @param longitude
	 * @param raioDeBusca
	 * @param filtro
	 * @param paginaAtual
	 * @param totalPorPagina
	 * @return
	 */
	public List<PontoDeInteresse> pontosPorProximidade(Double latitude, Double longitude, Double raioDeBusca, FiltroConsulta filtro, Integer paginaAtual, Integer totalPorPagina) {
		if (filtro == null)
			return pontosPorProximidade(latitude, longitude, raioDeBusca, paginaAtual, totalPorPagina);

		List<PontoDeInteresse> pontos = new ArrayList<>();
		DBCollection colecao = BaseDeDados.colecao("poi");

		BasicDBObject query = new BasicDBObject(); 
		Double[] posicao = {longitude, latitude};

		//A paǵina 1 vem como 0, para resovler sempre adicionar mais 1.
		paginaAtual += 1;
		int totalRegistros = paginaAtual * totalPorPagina;

		final BasicDBObject filter = new BasicDBObject("$nearSphere", posicao);
		filter.put("$maxDistance", raioDeBusca / CIRCUNFERENCIA_TERRA);
		query.put("posicao", filter);

		if (filtro.getCategoria() != null)
			query.put("categoria", filtro.getCategoria());
		if (filtro.getSubCategoria() != null)
			query.put("subCategoria", filtro.getSubCategoria());

		if (filtro.getPalavraChave() != null) {
			Pattern palavraChaveRgx = Pattern.compile(".*" + filtro.getPalavraChave() + ".*", Pattern.CASE_INSENSITIVE);
			BasicDBList dbListOr = new BasicDBList();

			/*
			 * Só considera a busca por palavra chave, para categoria e 
			 * subcategoria, se não informada uma categoria ou subcategoria específica.
			 */
			if (filtro.getSubCategoria() == null) {
				DBObject subCategoriaQuery = new BasicDBObject();
				subCategoriaQuery.put("subCategoria", palavraChaveRgx);
				dbListOr.add(subCategoriaQuery);
			}

			if (filtro.getCategoria() == null) {
				DBObject categoriaQuery = new BasicDBObject();
				categoriaQuery.put("categoria", palavraChaveRgx);
				dbListOr.add(categoriaQuery);
			}

			DBObject nomeQuery = new BasicDBObject();
			nomeQuery.put("nome", palavraChaveRgx);
			dbListOr.add(nomeQuery);

			DBObject or = new BasicDBObject();
			or.put("$or", dbListOr);

			BasicDBList dbListAnd = new BasicDBList();
			dbListAnd.add(or);
			query.append("$and", dbListAnd);
		}

		DBCursor resultado = colecao.find(query);
		if(resultado == null)
			return pontos;

		resultado = paginacao(totalRegistros, 10, null, resultado);

		for (DBObject poi : resultado.toArray()) {
			PontoDeInteresse ponto = new PontoDeInteresse();
			ponto.setIdCorporativo((Long) poi.get("idCorporativo"));
			ponto.setNome((String) poi.get("nome"));
			ponto.setCategoria((String) poi.get("categoria"));
			ponto.setSubCategoria((String) poi.get("subCategoria"));
			ponto.setLogradouro((String) poi.get("lograduro"));
			ponto.setPictograma((String) poi.get("pictograma"));
			ponto.setPossuiOferta((Boolean) poi.get("possuiOferta"));
			ponto.setTotalPessoasPresentes((Integer) poi.get("totalPessoasPresentes"));

			BasicDBList posicionamento = (BasicDBList) poi.get("posicao");
			ponto.setLongitude((Double) posicionamento.get(0));
			ponto.setLatitude((Double) posicionamento.get(1));

			Double distancia = GeoLocalizacao.calculoDistancia(ponto.getLatitude(), ponto.getLatitude(), latitude, longitude);
			BigDecimal d = new BigDecimal(distancia);
			d = d.setScale(0, RoundingMode.HALF_UP);
			ponto.setDistancia(d.doubleValue());

			pontos.add(ponto);
		}
		return OrdenacaoDistancia.ordenar(pontos);
	}

	public List<PontoDeInteresse> pontosPorPalavraChave(String palavraChave, Integer paginaAtual, Integer totalPorPagina) {
		List<PontoDeInteresse> pontos = new ArrayList<>();
		DBCollection colecao = BaseDeDados.colecao("poi");

		BasicDBObject query = new BasicDBObject(); 
		BasicDBList dbList = new BasicDBList();

		Pattern regex = Pattern.compile(".*" + palavraChave + ".*", Pattern.CASE_INSENSITIVE);

		DBObject subCategoriaQuery = new BasicDBObject();
		subCategoriaQuery.put("subCategoria", regex);
		dbList.add(subCategoriaQuery);

		DBObject categoriaQuery = new BasicDBObject();
		categoriaQuery.put("categoria", regex);
		dbList.add(categoriaQuery);

		DBObject nomeQuery = new BasicDBObject();
		nomeQuery.put("nome", regex);
		dbList.add(nomeQuery);

		query.append("$or", dbList);

		//A paǵina 1 vem como 0, para resovler sempre adicionar mais 1.
		paginaAtual += 1;
		int totalRegistros = paginaAtual * totalPorPagina;

		DBCursor resultado = colecao.find(query);
		if(resultado == null)
			return pontos;

		resultado = paginacao(totalRegistros, 10, null, resultado);

		for (DBObject poi : resultado.toArray()) {
			PontoDeInteresse ponto = new PontoDeInteresse();
			ponto.setIdCorporativo((Long) poi.get("idCorporativo"));
			ponto.setNome((String) poi.get("nome"));
			ponto.setCategoria((String) poi.get("categoria"));
			ponto.setSubCategoria((String) poi.get("subCategoria"));
			ponto.setLogradouro((String) poi.get("lograduro"));
			ponto.setPictograma((String) poi.get("pictograma"));
			ponto.setPossuiOferta((Boolean) poi.get("possuiOferta"));
			ponto.setTotalPessoasPresentes((Integer) poi.get("totalPessoasPresentes"));

			BasicDBList posicionamento = (BasicDBList) poi.get("posicao");
			ponto.setLongitude((Double) posicionamento.get(0));
			ponto.setLatitude((Double) posicionamento.get(1));
			pontos.add(ponto);
		}
		return pontos;
	}

	public List<String> categoriasPorProximidade(Double latitude, Double longitude, Double raioDeBusca) {
		List<String> categorias = new ArrayList<>(); 
		DBCollection colecao = BaseDeDados.colecao("poi");

		BasicDBObject fields = new BasicDBObject();
		fields.put("categoria", 1);

		Double[] posicao = {longitude, latitude};
		BasicDBObject query = new BasicDBObject();
		final BasicDBObject filter = new BasicDBObject("$nearSphere", posicao);
		filter.put("$maxDistance", raioDeBusca / CIRCUNFERENCIA_TERRA);
		query.put("posicao", filter);

		DBCursor resultados = colecao.find(query, fields);
		for (DBObject poi : resultados.toArray()) {
			categorias.add((String) poi.get("categoria"));
		}
		Collections.sort(categorias);
		return categorias;
	}
	
	public List<String> subCategoriasPorProximidade(Double latitude, Double longitude, Double raioDeBusca) {
		List<String> subCategorias = new ArrayList<>(); 
		DBCollection colecao = BaseDeDados.colecao("poi");

		BasicDBObject fields = new BasicDBObject();
		fields.put("subCategoria", 1);

		Double[] posicao = {longitude, latitude};
		BasicDBObject query = new BasicDBObject();
		final BasicDBObject filter = new BasicDBObject("$nearSphere", posicao);
		filter.put("$maxDistance", raioDeBusca / CIRCUNFERENCIA_TERRA);
		query.put("posicao", filter);

		DBCursor resultados = colecao.find(query, fields);
		for (DBObject poi : resultados.toArray()) {
			subCategorias.add((String) poi.get("subCategoria"));
		}
		Collections.sort(subCategorias);
		return subCategorias;
	}

	/**
	 * Adiona paginação e ordenção
	 * 
	 * @param start
	 * @param limit
	 * @param ordenarPor
	 * @param resultado
	 * @return
	 */
	private static DBCursor paginacao(int start, int limit, String ordenarPor, DBCursor resultado) {
        if (ordenarPor != null) {
        	String order = "desc";
        	DBObject sortCriteria = new BasicDBObject(ordenarPor, "desc".equals(order) ? -1 : 1); 
        	resultado.sort(sortCriteria);
        }
        resultado.skip(start).limit(limit);
        return resultado;
	}

}
