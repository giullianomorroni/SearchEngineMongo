package br.com.motorbusca.base;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.operation.CreateCollectionOperation;

public class BaseDeDados {

	/*
	 * Com somente uma instância o driver JDBC consegue gerenciar um POLL de conexões
	 * http://www.mongodb.org/display/DOCS/Java+Driver+Concurrency
	 */
	private static MongoClient mongo;
	private static String DATA_BASE_NAME = "POI";

	/*
	 * Usuário e senha para consulta e escrita no mongo
	 */
	private static char[] SENHA = "poi_poi_321!".toCharArray();
	private static String USUARIO = "poi";

	static {
		try {
			ServerAddress server = new ServerAddress("127.0.0.1", 37007);
			mongo = new MongoClient(server);
			//Configurando o poll para 30 conexões
			//MongoOptions options = new MongoOptions();
			//options.connectionsPerHost = 30;
			//mongo = new Mongo(server, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MongoCollection<Document> colecao(String nome) {
		MongoDatabase db = mongo.getDatabase(DATA_BASE_NAME);
		//db.authenticate(USUARIO, SENHA);
		mongo.setWriteConcern(WriteConcern.SAFE);
		MongoCollection<Document> collection = db.getCollection(nome);
		if (collection == null){
			db.createCollection(nome);
			collection = db.getCollection(nome);
		}
		collection.createIndex(new BasicDBObject("posicao", "2d"));
		return collection;
	}

	public static MongoDatabase conexao() {
		MongoDatabase db = mongo.getDatabase(DATA_BASE_NAME);
		//db.authenticate(USUARIO, SENHA);
		mongo.setWriteConcern(WriteConcern.SAFE);
		return db;
	}

	public static void apagarColecao(String nome) {
		MongoCollection<Document> collection = BaseDeDados.colecao(nome);
		collection.drop();
	}

}
