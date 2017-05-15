package br.com.cc.varzeafc.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cc.varzeafc.models.Posicao;

@Repository
public class PosicaoDAO {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Posicao posicao) {
		manager.persist(posicao);
	}

	public List<Posicao> listarTodos() {
		return manager.createQuery("select p from Posicao p", Posicao.class).getResultList();
	}

	public Posicao buscaPorId(Integer id) {
		return manager.find(Posicao.class, id);
	}

	public void atualizaPosicao(Posicao posicao) {
		manager.merge(posicao);

	}

	public void excluir(Posicao posicao) {
		manager.remove(posicao);

	}

}
