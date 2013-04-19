package br.com.motorbusca.base;

import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class BaseDeDados {

	/*
	 * Com somente uma instância o driver JDBC consegue gerenciar um POLL de conexões
	 * http://www.mongodb.org/display/DOCS/Java+Driver+Concurrency
	 */
	private static Mongo mongo;
	private static String DATA_BASE_NAME = "POI";

	/*
	 * Usuário e senha para consulta e escrita no mongo
	 */
	private static char[] SENHA = "poi_poi_321!".toCharArray();
	private static String USUARIO = "poi";

	static {
		try {
			//Configurando o poll para 30 conexões
			MongoOptions options = new MongoOptions();
			options.connectionsPerHost = 30;
			ServerAddress server = new ServerAddress("172.17.8.61", 37007);
			mongo = new Mongo(server, options);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static DBCollection colecao(String nome) {
		DB db = mongo.getDB(DATA_BASE_NAME);
		db.authenticate(USUARIO, SENHA);
		mongo.setWriteConcern(WriteConcern.SAFE);
		DBCollection collection = db.createCollection(nome, null);
		collection.ensureIndex(new BasicDBObject("posicao", "2d"));
		return collection;
	}

	public static DB conexao() {
		DB db = mongo.getDB(DATA_BASE_NAME);
		db.authenticate(USUARIO, SENHA);
		mongo.setWriteConcern(WriteConcern.SAFE);
		return db;
	}

	public static void apagarColecao(String nome) {
		DBCollection collection = BaseDeDados.colecao(nome);
		collection.drop();
	}

}
