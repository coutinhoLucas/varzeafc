package br.com.cc.varzeafc.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cc.varzeafc.models.LocalPartida;

@Repository
public class LocalDePartidaDAO {

	@PersistenceContext
	private EntityManager manager;

	public void salva(LocalPartida localDePartida) {
		manager.persist(localDePartida);
	}

	public List<LocalPartida> listarTodos() {
		return manager.createQuery("select l from LocalPartida l", LocalPartida.class).getResultList();
	}

	public LocalPartida buscaPorId(Integer id) {
		return manager.find(LocalPartida.class, id);
	}

	public void atualizaLocalPartida(LocalPartida localDePartida) {
		manager.merge(localDePartida);

	}

	public void excluir(LocalPartida localDePartida) {
		manager.remove(localDePartida);

	}

}
