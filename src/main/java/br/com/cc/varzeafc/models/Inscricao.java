package br.com.cc.varzeafc.models;

import java.time.LocalDate;
import java.util.List;

public class Inscricao {

	private Integer id;
	private Campeonato campeonato;
	private Equipe equipe;
	private Double valor;
	private LocalDate dataPagamento;
	private String statusPagamento;
	private List<Jogador> jogadores;

	public Campeonato getCampeonato() {
		return campeonato;
	}

	public void setCampeonato(Campeonato campeonato) {
		this.campeonato = campeonato;
	}

	public Equipe getEquipe() {
		return equipe;
	}

	public void setEquipe(Equipe equipe) {
		this.equipe = equipe;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(String statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public List<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}