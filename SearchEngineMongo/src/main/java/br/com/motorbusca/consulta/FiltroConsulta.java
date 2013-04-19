package br.com.motorbusca.consulta;

public class FiltroConsulta {

	private String categoria;
	private String subCategoria;
	private String palavraChave;
	
	public FiltroConsulta(String categoria, String subCategoria, String palavraChave) {
		super();
		this.categoria = categoria;
		this.subCategoria = subCategoria;
		this.palavraChave = palavraChave;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getSubCategoria() {
		return subCategoria;
	}

	public void setSubCategoria(String subCategoria) {
		this.subCategoria = subCategoria;
	}

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

}
