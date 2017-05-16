package br.com.cc.varzeafc.daos;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cc.varzeafc.models.Campeonato;
import br.com.cc.varzeafc.models.Jogador;

@Repository
public class JogadorDAO {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Jogador jogador) {
		manager.persist(jogador);
	}

	public List<Jogador> listarTodos() {
		return manager.createQuery("select j from Jogador j", Jogador.class).getResultList();
	}
	
	
	public List<Jogador> listaJogadoresSemInscricao() {
		return manager.createQuery(
				"select j from Jogador j where j.inscricao is null", Jogador.class).getResultList();
	}

	public Jogador buscaPorId(Integer id) {
		return manager.find(Jogador.class, id);
	}

	public void atualizaJogador(Jogador jogador) {
		manager.merge(jogador);

	}

	public void excluir(Jogador jogador) {
		manager.remove(jogador);

	}

}
