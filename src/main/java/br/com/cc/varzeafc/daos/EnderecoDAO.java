package br.com.cc.varzeafc.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cc.varzeafc.models.Endereco;

@Repository
public class EnderecoDAO {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Endereco endereco) {
		manager.persist(endereco);
	}

	public List<Endereco> listarTodos() {
		return manager.createQuery("select e from Endereco e", Endereco.class).getResultList();
	}

	public Endereco buscaPorId(Integer id) {
		return manager.find(Endereco.class, id);
	}

	public void atualizaLocalPartida(Endereco endereco) {
		manager.merge(endereco);

	}

	public void excluir(Endereco endereco) {
		manager.remove(endereco);

	}

}
