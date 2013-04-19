package br.com.motorbusca.modelo;

import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import br.com.motorbusca.fonetica.GeradorFonetica;

public class PontoDeInteresse implements Serializable {

	private static final long serialVersionUID = -1684649132896438100L;

	private static final Properties icones = new Properties();

	static {
		try {
			icones.load(new FileReader("/opt/flagme/categorias/categorias.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Long idCorporativo;
	private String nome;
	private String fonema;
	private String pictograma;
	private String categoria;
	private String subCategoria;
	private String logradouro;
	private String cidade;
	private String estado;
	private String cep;
	private Double latitude;
	private Double longitude;
	private Double distancia = 0D;
	private Integer totalPessoasPresentes = 0;
	private Boolean possuiOferta = false;

	public PontoDeInteresse() {
		super();
	}

	public PontoDeInteresse(Long idCorporativo, String nome,
			String categoria, String subCategoria,
			String logradouro, String cidade, String estado, String cep,
			Double latitude, Double longitude) {
		super();
		this.idCorporativo = idCorporativo;
		this.nome = nome;
		this.categoria = categoria;
		this.subCategoria = subCategoria;
		this.logradouro = logradouro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
		this.latitude = latitude;
		this.longitude = longitude;
		this.pictograma = buscarIcone(subCategoria);
		if (this.pictograma == null)
			this.pictograma = buscarIcone(categoria);
		this.fonema = GeradorFonetica.criarFonema(nome);
	}

	public String buscarIcone(String descricao) {
		if (descricao == null)
			return "";

		String aux = descricao;
		String nomeCategoria =  aux.toLowerCase()
				.replace("-", "").replace("/", "_e_").replace("  ", "_").replace(" ", "_").replace("__", "_") //dois espacos em branco e um espaco em branco
				.replace("ã", "a").replace("á", "a").replace("â", "a").replace("à", "a")
				.replace("é", "e").replace("ẽ", "e").replace("ê", "e")
				.replace("ó", "o").replace("õ", "o").replace("ô", "o")
				.replace("í", "i")
				.replace("ú", "u")
				.replace("ç", "c");

		String iconeCategoria = icones.getProperty(nomeCategoria);
		return iconeCategoria;
	}

	public Long getIdCorporativo() {
		return idCorporativo;
	}

	public void setIdCorporativo(Long idCorporativo) {
		this.idCorporativo = idCorporativo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFonema() {
		return fonema;
	}

	public void setFonema(String fonema) {
		this.fonema = fonema;
	}

	public String getPictograma() {
		return pictograma;
	}

	public void setPictograma(String pictograma) {
		this.pictograma = pictograma;
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

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getDistancia() {
		return distancia;
	}

	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}

	public Integer getTotalPessoasPresentes() {
		return totalPessoasPresentes;
	}

	public void setTotalPessoasPresentes(Integer totalPessoasPresentes) {
		this.totalPessoasPresentes = totalPessoasPresentes;
	}

	public Boolean getPossuiOferta() {
		return possuiOferta;
	}

	public void setPossuiOferta(Boolean possuiOferta) {
		this.possuiOferta = possuiOferta;
	}

}
