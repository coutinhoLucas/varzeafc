package br.com.cc.varzeafc.daos;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import br.com.cc.varzeafc.models.Inscricao;
import br.com.cc.varzeafc.models.Usuario;

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

	public void update(Inscricao inscricao) {
		manager.merge(inscricao);
	}

	public Inscricao getInscricaoPeloIdDeEquipeEcampeonatoAtivo(Integer idEquipe, Integer idCampeonato) {

		try {
			return manager
					.createQuery(
							"from Inscricao as i where i.equipe.id =:idEquipe and i.campeonato.id =:idCampeonato",
							Inscricao.class)
					.setParameter("idEquipe", idEquipe).setParameter("idCampeonato", idCampeonato).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Inscricao getInscricaoPeloCodigoPagamento(String codigoPagamento) throws SQLException {
		
		try {
			
			CriteriaBuilder builder = manager.getCriteriaBuilder();
			CriteriaQuery<Inscricao> criteria = builder.createQuery(Inscricao.class);
			Root<Inscricao> root = criteria.from(Inscricao.class);
			Predicate like = builder.like(root.<String>get("codigoPagamento"), codigoPagamento);
			return manager.createQuery(criteria.select(root).where(like)).getSingleResult();
			
		}catch (NoResultException  e) {
			throw new SQLException();
		}
	}

}
