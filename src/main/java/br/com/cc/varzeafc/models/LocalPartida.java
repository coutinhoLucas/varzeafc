package br.com.cc.varzeafc.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class LocalPartida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nomeLocalPartida;
	@OneToOne(cascade = CascadeType.PERSIST)
	private Endereco endereco;
	@OneToMany(mappedBy="localDaPartida")
	private List<Jogo> jogos;

	public String getNomeLocalPartida() {
		return nomeLocalPartida;
	}

	public void setNomeLocalPartida(String nomeLocalPartida) {
		this.nomeLocalPartida = nomeLocalPartida;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public List<Jogo> getJogos() {
		return jogos;
	}

	public void setJogos(List<Jogo> jogos) {
		this.jogos = jogos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
