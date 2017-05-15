package br.com.cc.varzeafc.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotBlank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

@Entity
public class Campeonato implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "Nome é obrigatório")
	private String nome;
	@Temporal(TemporalType.DATE)
	private Calendar dataCriacao;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "CAMPEONATO_PATROCINADOR", joinColumns = @JoinColumn(name = "campeonato_id"), inverseJoinColumns = @JoinColumn(name = "patrocinador_id"))
	private List<Patrocinador> patrocinadores;
	@ManyToOne
	private Temporada temporada;
	@Enumerated(EnumType.STRING)
	private CampeonatoStatus status;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "INSCRICAO", joinColumns = @JoinColumn(name = "campeonato_id"), inverseJoinColumns = @JoinColumn(name = "equipe_id"))
	private List<Equipe> equipes;
	@OneToMany(mappedBy = "campeonato")
	private List<Rodada> rodadas;
	private Double taxaDeInscricao;
	@Lob
	private String descricao;
	private Integer numeroDeEquipes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Calendar getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Calendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<Patrocinador> getPatrocinadores() {
		return patrocinadores;
	}

	public void setPatrocinadores(List<Patrocinador> patrocinadores) {
		this.patrocinadores = patrocinadores;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}

	public CampeonatoStatus getStatus() {
		return status;
	}

	public void setStatus(CampeonatoStatus status) {
		this.status = status;
	}

	public List<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(List<Equipe> equipes) {
		this.equipes = equipes;
	}

	public List<Rodada> getRodadas() {
		return rodadas;
	}

	public void setRodadas(List<Rodada> rodadas) {
		this.rodadas = rodadas;
	}

	public Double getTaxaDeInscricao() {
		return taxaDeInscricao;
	}

	public void setTaxaDeInscricao(Double taxaDeInscricao) {
		this.taxaDeInscricao = taxaDeInscricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getNumeroDeEquipes() {
		return numeroDeEquipes;
	}

	public void setNumeroDeEquipes(Integer numeroDeEquipes) {
		this.numeroDeEquipes = numeroDeEquipes;
	}

}
