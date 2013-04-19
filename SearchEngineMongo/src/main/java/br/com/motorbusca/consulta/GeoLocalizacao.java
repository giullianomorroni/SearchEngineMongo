package br.com.motorbusca.consulta;

class GeoLocalizacao {

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
	static Double calculoDistancia(Double latitude_1, Double longitude_1, Double latitude_2, Double longitude_2) {

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

}
