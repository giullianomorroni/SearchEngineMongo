package br.com.motorbusca.consulta;

import java.util.List;

import br.com.motorbusca.modelo.PontoDeInteresse;

class OrdenacaoDistancia {

	/**
	 * Ordena uma lista de {@link PontoDeInteresse} pela distância da menor para  maior.
	 * 
	 * @param lista
	 * @return
	 */
	static List<PontoDeInteresse> ordenar(List<PontoDeInteresse> lista) {
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
