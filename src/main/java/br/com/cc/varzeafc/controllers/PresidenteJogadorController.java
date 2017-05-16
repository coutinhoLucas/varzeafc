package br.com.cc.varzeafc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.cc.varzeafc.conf.UsuarioSistema;
import br.com.cc.varzeafc.daos.EquipeDAO;
import br.com.cc.varzeafc.daos.InscricaoDAO;
import br.com.cc.varzeafc.daos.JogadorDAO;
import br.com.cc.varzeafc.daos.PosicaoDAO;
import br.com.cc.varzeafc.models.Equipe;
import br.com.cc.varzeafc.models.Inscricao;
import br.com.cc.varzeafc.models.Jogador;
import br.com.cc.varzeafc.models.Posicao;

@Controller
@Transactional
@Scope("session")
public class PresidenteJogadorController {

	@Autowired
	private JogadorDAO jogadorDAO;

	@Autowired
	private PosicaoDAO posicaoDAO;
	
	@Autowired
	private EquipeDAO equipeDAO;
	
	@Autowired
	private InscricaoDAO inscricaoDAO;
	
	@RequestMapping(method = RequestMethod.GET, value = "jogador")
	public ModelAndView form(Jogador jogador) {
		ModelAndView view = new ModelAndView("jogador/add-jogador");
		view.addObject("posicoes", carregaPosicoes());
		return view;
	}

	private List<Posicao> carregaPosicoes() {
		return posicaoDAO.listarTodos();
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "jogador")
	@CacheEvict(value = "jogadores", allEntries = true)
	public ModelAndView addJogador(@Valid Jogador jogador, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return form(jogador);
		}
		

		jogadorDAO.salva(jogador);

		redirectAttributes.addFlashAttribute("mensagem", "Jogador cadastrado com sucesso.");
		return new ModelAndView("redirect:/jogador");
	}

	@RequestMapping(method = RequestMethod.GET, value = "jogadores")
	@Cacheable("jogadores")
	public ModelAndView list(Jogador jogador) {
		ModelAndView view = new ModelAndView("jogador/list-jogadores");
		
		view.addObject("jogadores", jogadorDAO.listarTodos());
		
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "jogador/{id}")
	public ModelAndView carregaJogador(@PathVariable("id") Integer id) {

		ModelAndView view = new ModelAndView("jogador/update-jogador");
		view.addObject("jogador", jogadorDAO.buscaPorId(id));
		view.addObject("posicoes", carregaPosicoes());

		return view;
	}

	@RequestMapping(method = RequestMethod.POST, value = "jogador/{id}")
	@CacheEvict(value = "jogadores", allEntries = true)
	public ModelAndView update(@PathVariable("id") Integer id, @Valid Jogador jogador, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return list(jogador);
		}

		jogadorDAO.atualizaJogador(jogador);
		return new ModelAndView("redirect:/jogadores");
	}

	@RequestMapping(method = RequestMethod.GET, value = "jogador/excluir/{id}")
	@CacheEvict(value = "jogadores", allEntries = true)
	public String remove(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		Jogador jogador = jogadorDAO.buscaPorId(id);
		
		if(jogador.getInscricao() == null){
			jogadorDAO.excluir(jogador);
			
			return "redirect:/jogadores";
		}
		
		redirectAttributes.addFlashAttribute("mensagem", "Não foi possivel excluir o jogador, pois está inscrito em um campeonato.");
		return "redirect:/jogadores";
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "inscreverJogadores")
	@Cacheable("jogadores")
	public ModelAndView listarAssociar(ArrayList<Jogador> jogadores) {
		ModelAndView view = new ModelAndView("jogador/inscrever-jogadores");
		
		view.addObject("jogadores", jogadorDAO.listaJogadoresSemInscricao());
		
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "inscreverJogadores")
	@CacheEvict(value = "jogadores", allEntries = true)
	public ModelAndView inscreverJogadores(@Valid ArrayList<Jogador> jogadores, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return listarAssociar((ArrayList<Jogador>) jogadorDAO.listaJogadoresSemInscricao());
		}
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioSistema usuario = (UsuarioSistema) auth.getPrincipal();
		Equipe equipe = equipeDAO.buscaEquipePorIdPresidente(usuario.getId());
		Inscricao inscricao = inscricaoDAO.buscaPorIdEquipe(equipe.getId());
		
		if(inscricao != null){
			inscricao.setJogadores(jogadores);
		}
		
		redirectAttributes.addFlashAttribute("mensagem", "Jogador cadastrado com sucesso.");
		return listarAssociar((ArrayList<Jogador>) jogadorDAO.listaJogadoresSemInscricao());
	}


}