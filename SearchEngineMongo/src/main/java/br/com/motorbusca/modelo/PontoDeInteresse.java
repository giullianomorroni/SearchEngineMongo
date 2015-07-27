package br.com.motorbusca.modelo;

import java.io.Serializable;


public class PontoDeInteresse implements Serializable {

	private static final long serialVersionUID = -1684649132896438100L;

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

	public PontoDeInteresse() {
		super();
	}

	public PontoDeInteresse(String nome, String categoria) {
		super();
		this.nome = nome;
		this.fonema = Fonetica.criarFonema(nome);
		this.categoria = categoria;
	}

	public PontoDeInteresse endereco(String logradouro, String cidade, String estado, String cep, Double latitude, Double longitude) {
		this.logradouro = logradouro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
		this.latitude = latitude;
		this.longitude = longitude;
		return this;
	}

	public PontoDeInteresse(String nome, String fonema, String pictograma,
			String categoria, String subCategoria, String logradouro,
			String cidade, String estado, String cep, Double latitude,
			Double longitude, Double distancia) {
		super();
		this.nome = nome;
		this.fonema = Fonetica.criarFonema(nome);
		this.pictograma = pictograma;
		this.categoria = categoria;
		this.subCategoria = subCategoria;
		this.logradouro = logradouro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distancia = distancia;
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

}
