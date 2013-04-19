package br.com.motorbusca.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaConexao {

	private static String url = "jdbc:mysql://projeto9.saveme.com.br:3306/";
	private static String dbName = "POI";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String userName = "poi";
	private static String password = "poi_poi_321!";


	/**
	 * Este método não utilza um poll de conexão.
	 * @return
	 */
	public static Connection criarConexao() {
		try {
			Class.forName(driver).newInstance();
			return DriverManager.getConnection(url + dbName, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void fechar(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
