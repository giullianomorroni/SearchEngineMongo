package br.com.motorbusca.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.BsonArray;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.Document;

import br.com.motorbusca.modelo.PontoDeInteresse;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;

public class CargaDeDadosFake {

	static List<DBObject> registros = new ArrayList<>();
	static List<PontoDeInteresse> pois = new ArrayList<>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PontoDeInteresse poi = new PontoDeInteresse("Maria Juju", "Depiladora");
		poi.endereco("Rua Flórida- Itaim Bibi", "São Paulo", "SP", "04565-001", -23.606551, -46.696678);
		pois.add(poi);

		poi = new PontoDeInteresse("Antonia", "Manicure");
		poi.endereco("Rua Flórida- Itaim Bibi", "São Paulo", "SP", "04565-001", -23.607289, -46.694393);
		pois.add(poi);

		MongoCollection<Document> collection = BaseDeDados.colecao("poi");

		for (PontoDeInteresse p : pois) {
			Document doc = new Document();
			doc.put("nome", p.getNome());
			doc.put("fonema", p.getFonema());

			doc.put("categoria", p.getCategoria());
			doc.put("subCategoria", p.getSubCategoria());
			doc.put("pictograma", p.getPictograma());

			//No modo spherical é preciso usar longitude/latitude (inverter gera resultados imprevisiveis)
			//doc.put("posicao", new Double[] {, p.getLatitude()});
			doc.put("posicao", new BsonArray(Arrays.asList(new BsonDouble(p.getLongitude()), new BsonDouble(p.getLatitude()))));

			doc.put("logradouro", p.getLogradouro());
			doc.put("cidade", p.getCidade());
			doc.put("estado", p.getEstado());
			doc.put("cep", p.getCep());
			//registros.add(doc);
			try {
				collection.insertOne(doc);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Erro:" + p.getNome());
			}
		}

	}

}
