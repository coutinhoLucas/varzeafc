package br.com.cc.varzeafc.models;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.paypal.api.payments.Payment;

import br.com.cc.varzeafc.conf.UsuarioSistema;
import br.com.cc.varzeafc.daos.EquipeDAO;
import br.com.cc.varzeafc.daos.InscricaoDAO;

@Entity
@Table(name = "INSCRICAO")
public class Inscricao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne
	private Campeonato campeonato;
	@ManyToOne
	private Equipe equipe;
	private Double valor;
	@Temporal(TemporalType.DATE)
	private Calendar dataPagamento;
	private String statusPagamento;
	private String codigoPagamento;
	@OneToMany(mappedBy = "inscricao")
	private List<Jogador> jogadores;

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Calendar getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Calendar dataPagamento) {
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

	public String getCodigoPagamento() {
		return codigoPagamento;
	}

	public void setCodigoPagamento(String codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}

	public void salvaDadosInscricao(Payment payment, InscricaoDAO inscricaoDAO, EquipeDAO equipeDAO, Double valorInscricao) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioSistema usuario = (UsuarioSistema) auth.getPrincipal();
		this.setEquipe(equipeDAO.buscaEquipePorIdPresidente(usuario.getId()));
		this.setDataPagamento(Calendar.getInstance());
		this.setCodigoPagamento(payment.getId());
		this.setStatusPagamento(payment.getState());
		this.setValor(valorInscricao);
		inscricaoDAO.salva(this);
		
	}

}
