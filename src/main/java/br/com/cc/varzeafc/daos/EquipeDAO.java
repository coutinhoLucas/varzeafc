package br.com.cc.varzeafc.daos;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import br.com.cc.varzeafc.models.Equipe;

@Repository
public class EquipeDAO {

	@PersistenceContext
	private EntityManager manager;

	public Equipe buscaEquipePorIdPresidente(Integer id) {

		return manager.createQuery("select e from Equipe e JOIN e.presidente p where p.id =:id", Equipe.class)
				.setParameter("id", id).getSingleResult();

	}

}
