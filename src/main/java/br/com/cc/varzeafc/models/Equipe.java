package br.com.cc.varzeafc.models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Equipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToMany(mappedBy = "equipes")
	private List<Campeonato> campeonatos;
	private String nome;
	private String sigla;
	@Temporal(TemporalType.DATE)
	private Calendar dataCriacao;
	@OneToOne
	@JoinColumn(name = "presidente_id")
	private Presidente presidente;
	@OneToMany(mappedBy = "equipeMandante")
	private List<Jogo> jogosMandante;
	@OneToMany(mappedBy = "equipeVisitante")
	private List<Jogo> jogosVisitante;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Campeonato> getCampeonatos() {
		return campeonatos;
	}

	public void setCampeonatos(List<Campeonato> campeonatos) {
		this.campeonatos = campeonatos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public Calendar getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Calendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Presidente getPresidente() {
		return presidente;
	}

	public void setPresidente(Presidente presidente) {
		this.presidente = presidente;
	}

	public List<Jogo> getJogosMandante() {
		return jogosMandante;
	}

	public void setJogosMandante(List<Jogo> jogosMandante) {
		this.jogosMandante = jogosMandante;
	}

	public List<Jogo> getJogosVisitante() {
		return jogosVisitante;
	}

	public void setJogosVisitante(List<Jogo> jogosVisitante) {
		this.jogosVisitante = jogosVisitante;
	}

	public Campeonato buscaCampeonatoAberto() {
		
		Campeonato campeonatoAtivo = null;
		
		for (Campeonato campeonato : this.getCampeonatos()) {
			if(campeonato.getStatus().name().equals("ABERTO")){
				campeonatoAtivo = campeonato;
			}
		}
		
		return campeonatoAtivo;
		
	}
}
