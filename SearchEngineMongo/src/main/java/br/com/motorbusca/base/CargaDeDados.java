package br.com.motorbusca.base;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.motorbusca.modelo.Fonetica;
import br.com.motorbusca.modelo.PontoDeInteresse;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class CargaDeDados {

	static List<DBObject> registros = new ArrayList<>();
	static List<PontoDeInteresse> pois = new ArrayList<>();

	/**
	 * @param args
	 * @throws UnknownHostException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws UnknownHostException, SQLException {

		String SQL = "SELECT " +  
					" ESTABELECIMENTO.ID AS ID_ESTABELECIMENTO, " +
					" ESTABELECIMENTO.NOME_FANTASIA AS NOME_FANTASIA, " +
					" CATEGORIA.DESCRICAO AS CATEGORIA,  " +
					" SUBCATEGORIA.DESCRICAO AS SUBCATEGORIA, " + 
					" ENDERECO.LOGRADOURO AS LOGRADOURO,  " +
					" ENDERECO.NUMERO AS NUMERO,  " +
					" ENDERECO.CIDADE AS CIDADE, " +
					" ENDERECO.BAIRRO AS BAIRRO, " +
					" ENDERECO.CEP AS CEP, " +
					" ENDERECO.ESTADO AS ESTADO, " + 
					" ENDERECO.LATITUDE AS LATITUDE,  " +
					" ENDERECO.LONGITUDE AS LONGITUDE " +
				 " FROM TB_ENDERECO AS ENDERECO  " +
					 " INNER JOIN TB_ESTABELECIMENTO AS ESTABELECIMENTO ON ESTABELECIMENTO.ID_ENDERECO = ENDERECO.ID " + 
					 " INNER JOIN TB_CATEGORIA AS CATEGORIA ON CATEGORIA.ID = ESTABELECIMENTO.ID_CATEGORIA  " +
					 " LEFT JOIN TB_SUBCATEGORIA AS SUBCATEGORIA ON SUBCATEGORIA.ID = ESTABELECIMENTO.ID_SUBCATEGORIA " + 
				 " WHERE ENDERECO.LATITUDE IS NOT NULL AND ENDERECO.LATITUDE != -1 ";

		Connection conexao = FabricaConexao.criarConexao();
		PreparedStatement ps = conexao.prepareStatement(SQL);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			PontoDeInteresse poi = new PontoDeInteresse();
			poi.setNome(rs.getString("NOME_FANTASIA"));
			poi.setCategoria(rs.getString("CATEGORIA"));
			poi.setSubCategoria(rs.getString("SUBCATEGORIA")); 
			String b = rs.getString("BAIRRO");
			String l = rs.getString("LOGRADOURO");
			String n = rs.getString("NUMERO");
			if (n != null)
				l = l + ", " +n;
			if (b != null && b.trim().length() > 5)
				poi.setLogradouro(l +" - "+b);
			else
				poi.setLogradouro(l);
			poi.setCidade(rs.getString("CIDADE"));
			poi.setCep(rs.getString("CEP"));
			poi.setEstado(rs.getString("ESTADO")); 
			poi.setLatitude(rs.getDouble("LATITUDE"));
			poi.setLongitude(rs.getDouble("LONGITUDE"));
			poi.setFonema(Fonetica.criarFonema(poi.getNome()));
			pois.add(poi);
		}
		conexao.close();

		System.out.println("Foram encontrados " + pois.size());

		DBCollection collection = BaseDeDados.colecao("poi");
		for (PontoDeInteresse p : pois) {
			DBObject doc = new BasicDBObject();
			doc.put("nome", p.getNome());
			doc.put("fonema", p.getFonema());

			doc.put("categoria", p.getCategoria());
			doc.put("subCategoria", p.getSubCategoria());
			doc.put("pictograma", p.getPictograma());

			//No modo spherical é preciso usar longitude/latitude (inverter gera resultados imprevisiveis)
			doc.put("posicao", new Double[] {p.getLongitude(), p.getLatitude()});
			doc.put("logradouro", p.getLogradouro());
			doc.put("cidade", p.getCidade());
			doc.put("estado", p.getEstado());
			doc.put("cep", p.getCep());
			//registros.add(doc);
			try {
				collection.insert(doc);
			} catch (Exception e) {
				System.out.println("Erro:" + p.getNome());
			}
		}

	}

}
