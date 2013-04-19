package br.com.motorbusca;

import java.sql.SQLException;
import java.util.List;

import br.com.motorbusca.consulta.Consulta;
import br.com.motorbusca.consulta.FiltroConsulta;
import br.com.motorbusca.modelo.PontoDeInteresse;

public class TesteMongoDB {

	public static void main(String[] args) throws SQLException {
		//consultaPorProximidade(-23.568570, -46.647563, 2000.0, 0, 10);
		//consultaPorProximidade(-23.568570, -46.647563, 2000.0, "Bar", 1, 10);
		//consultaPorPalavraChave("banco", 0, 10);
		consutarCategoriasPorProximidade(-23.568570, -46.647563, 2000.0);
	}

	private static void consutarCategoriasPorProximidade(Double latitude, Double longitude, Double raio) {
		Consulta consulta = new Consulta();
		List<String> categorias = consulta.categoriasPorProximidade(latitude, longitude, raio);
		for (String string : categorias)
			System.out.println(string);
	}

	public static void consultaPorProximidade(Double latitude, Double longitude, Double raio, Integer pagina, Integer quantidade) {
		Consulta consulta = new Consulta();
		List<PontoDeInteresse> pontos = consulta.pontosPorProximidade(latitude, longitude, raio, pagina, quantidade);
		for (PontoDeInteresse ponto : pontos)
			System.out.println("NOME:" + ponto.getNome() +" CAT/SUB:"+ ponto.getCategoria() + "/" + ponto.getSubCategoria() + " DISTANCIA:"+ ponto.getDistancia()+" LATITUDE/LONGITUDE:"+ ponto.getLatitude() + "/" + ponto.getLongitude());
	}

	public static void consultaPorProximidade(Double latitude, Double longitude, Double raio, String palavraChave, Integer pagina, Integer quantidade) {
		Consulta consulta = new Consulta();
		List<PontoDeInteresse> pontos = consulta.pontosPorProximidade(latitude, longitude, raio, new FiltroConsulta(null, null, "anc"), pagina, quantidade);
		for (PontoDeInteresse ponto : pontos)
			System.out.println("NOME:" + ponto.getNome() +" CAT/SUB:"+ ponto.getCategoria() + "/" + ponto.getSubCategoria() + " DISTANCIA:"+ ponto.getDistancia()+" LATITUDE/LONGITUDE:"+ ponto.getLatitude() + "/" + ponto.getLongitude());
	}

	public static void consultaPorPalavraChave(String palavraChave, Integer pagina, Integer quantidade) {
		Consulta consulta = new Consulta();
		List<PontoDeInteresse> pontos = consulta.pontosPorPalavraChave(palavraChave, pagina, quantidade);
		for (PontoDeInteresse ponto : pontos)
			System.out.println("NOME:" + ponto.getNome() +" CAT/SUB:"+ ponto.getCategoria() + "/" + ponto.getSubCategoria() + " DISTANCIA:"+ ponto.getDistancia()+" LATITUDE/LONGITUDE:"+ ponto.getLatitude() + "/" + ponto.getLongitude());
	}

}
