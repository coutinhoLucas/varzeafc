package br.com.cc.varzeafc.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.remoting.caucho.BurlapServiceExporter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.cc.varzeafc.conf.UsuarioSistema;
import br.com.cc.varzeafc.daos.EquipeDAO;
import br.com.cc.varzeafc.daos.InscricaoDAO;
import br.com.cc.varzeafc.daos.JogadorDAO;
import br.com.cc.varzeafc.daos.PosicaoDAO;
import br.com.cc.varzeafc.models.Campeonato;
import br.com.cc.varzeafc.models.Equipe;
import br.com.cc.varzeafc.models.Inscricao;
import br.com.cc.varzeafc.models.InscricaoJogadorCampeonato;
import br.com.cc.varzeafc.models.Jogador;
import br.com.cc.varzeafc.models.Posicao;

@Controller
@Transactional
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RequestMapping("/varzeafc")
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
	public ModelAndView form(Jogador jogador, RedirectAttributes redirectAttributes) {
		
		ModelAndView view = new ModelAndView("jogador/add-jogador");

		Equipe equipe = equipeDAO.buscaEquipePorIdPresidente(getUsuarioLogado().getId());
		Campeonato campeonatoAberto = equipe.buscaCampeonatoAberto();

		if (campeonatoAberto == null) {
			redirectAttributes.addFlashAttribute("erro","Sua equipe não possui uma inscrição em campeonato.");
			return new ModelAndView("redirect:/varzeafc");
		}

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
			return new ModelAndView("jogador/add-jogador").addObject("posicoes",carregaPosicoes());
		}
		
		Equipe equipe = equipeDAO.buscaEquipePorIdPresidente(getUsuarioLogado().getId());
		Campeonato campeonatoAberto = equipe.buscaCampeonatoAberto();
		Inscricao inscricao = inscricaoDAO.getInscricaoPeloIdDeEquipeEcampeonatoAtivo(equipe.getId(), campeonatoAberto.getId());
		
		jogador.setInscricao(inscricao);
		jogadorDAO.salva(jogador);
		
		redirectAttributes.addFlashAttribute("mensagem", "Jogador cadastrado com sucesso.");
		return new ModelAndView("redirect:varzeafc/jogador");
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

		if (jogador.getInscricao() == null) {
			jogadorDAO.excluir(jogador);

			return "redirect:/jogadores";
		}

		redirectAttributes.addFlashAttribute("mensagem",
				"Não foi possivel excluir o jogador, pois está inscrito em um campeonato.");
		return "redirect:/jogadores";
	}

	@RequestMapping(method = RequestMethod.GET, value = "inscreverJogadores")
	@Cacheable("jogadores")
	public ModelAndView listarAssociar(InscricaoJogadorCampeonato lisJogadores) {
		ModelAndView view = new ModelAndView("jogador/inscrever-jogadores");

		view.addObject("allJogadores", jogadorDAO.listaJogadoresSemInscricao());

		return view;
	}

/*	@RequestMapping(method = RequestMethod.POST, value = "inscreverJogadores")
	@CacheEvict(value = "jogadores", allEntries = true)
	public ModelAndView inscreverJogadores(InscricaoJogadorCampeonato listJogadores, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors() || listJogadores == null) {
			return listarAssociar(listJogadores);
		}

		Equipe equipe = equipeDAO.buscaEquipePorIdPresidente(usuario.getId());
		Campeonato campeonatoAtivo = equipe.verificaCampeonatoAtivo();
		if (campeonatoAtivo == null) {
			redirectAttributes.addFlashAttribute("mensagem", "Sua equipe não possui uma inscrição em campeonato.");
			return listarAssociar(listJogadores);
		}

		Inscricao inscricao = inscricaoDAO.buscaPorIdEquipe(equipe.getId(), campeonatoAtivo.getId());

		inscricao.setJogadores(listJogadores.getJogadores());
		inscricaoDAO.update(inscricao);

		redirectAttributes.addFlashAttribute("mensagem",
				"Jogadores inscritos no campeonato " + inscricao.getCampeonato().getNome());
		return listarAssociar(listJogadores);
	}*/

	private UsuarioSistema getUsuarioLogado() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioSistema usuario = (UsuarioSistema) auth.getPrincipal();

		return usuario;

	}

}
