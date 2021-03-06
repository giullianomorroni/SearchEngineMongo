package br.com.motorbusca.modelo;

import java.util.List;


public class Distancia {

	static int EARTH_RADIUS_KM = 6371;
	static int EARTH_RADIUS_M = EARTH_RADIUS_KM * 1000; //cada km tem 1.000 m

	/**
	 * Devole a distância em metros.
	 */
	public static String METROS = "M";

	/**
	 * Calculo da distancia entre dois pontos (lat/long) geográficos e devolve
	 * a resposta utilizando a unidade de medidas METROS. 
	 * 
	 * @param latitude_1
	 * @param longitude_1
	 * @param latitude_2
	 * @param longitude_2
	 * @return
	 * @author giulliano.morroni
	 */
	public static Double calculoDistancia(Double latitude_1, Double longitude_1, Double latitude_2, Double longitude_2) {

		if (latitude_1 == null || longitude_1 == null || latitude_2 == null || longitude_2 == null)
			return 0D;

		// Conversão de graus pra radianos das latitudes
		double latitudeEmDecimal_1 = Math.toRadians(latitude_1);
		double latitudeEmDecimal_2 = Math.toRadians(latitude_2);

		// Diferença das longitudes
		double diferenciaLongitudes = Math.toRadians(longitude_2 - longitude_1);

		//aqui usei como padrão a unidade de metros
		int medida = EARTH_RADIUS_M; 

		// Cálcula da distância entre os pontos
		Double distancia = Math.acos(Math.cos(latitudeEmDecimal_1) * Math.cos(latitudeEmDecimal_2) 
				* Math.cos(diferenciaLongitudes) + Math.sin(latitudeEmDecimal_1)
				* Math.sin(latitudeEmDecimal_2))
				* medida;

		return distancia;
	}

	/**
	 * Ordena uma lista de {@link PontoDeInteresse} pela distância da menor para  maior.
	 * 
	 * @param lista
	 * @return
	 */
	public static List<PontoDeInteresse> ordenar(List<PontoDeInteresse> lista) {
		try {
			Double di = 0d;
			Double dj = 0d;

			for (int i=0; i < lista.size(); i++) {

				if (lista.size() < i+1)
					break;

				PontoDeInteresse model_1 = lista.get(i);
				PontoDeInteresse model_2 = lista.get(i+1);

				di = Double.valueOf(model_1.getDistancia());
				dj = Double.valueOf(model_2.getDistancia());

				if (di > dj) {
					PontoDeInteresse menor = model_2;
					lista.remove(menor);
					lista.add(i, menor);
					ordenar(lista);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// Fim da ordenação
		}
		return lista;
	}

}
