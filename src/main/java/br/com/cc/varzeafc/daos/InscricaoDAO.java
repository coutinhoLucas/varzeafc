package br.com.cc.varzeafc.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cc.varzeafc.models.Inscricao;

@Repository
public class InscricaoDAO {

	@PersistenceContext
	private EntityManager manager;

	public void salva(Inscricao inscricao) {
		manager.persist(inscricao);

	}
	
	public Inscricao buscaPorId(Integer id) {
		return manager.find(Inscricao.class, id);
	}
	
	public Inscricao buscaPorIdEquipe(Integer id) {

		return manager.createQuery("select i from Inscricao i JOIN i.equipe e where e.id = :id", Inscricao.class)
				.setParameter("id", id).getSingleResult();

	}

}
