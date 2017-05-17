package br.com.cc.varzeafc.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import br.com.cc.varzeafc.daos.PosicaoDAO;
import br.com.cc.varzeafc.models.Posicao;;

@Controller
@Transactional
@RequestMapping("/admin")
@Scope(value=WebApplicationContext.SCOPE_REQUEST)
public class AdminPosicaoJogadorController {

	@Autowired
	private PosicaoDAO posicaoDAO;
	
	@RequestMapping(method = RequestMethod.GET, value = "posicao")
	public ModelAndView form(Posicao posicao) {
		return new ModelAndView("posicao/add-posicao");
	}

	@RequestMapping(method = RequestMethod.POST, value = "posicao")
	@CacheEvict(value = "posicoes", allEntries = true)
	public ModelAndView add(@Valid Posicao posicao, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return form(posicao);

		}
		
		posicaoDAO.salva(posicao);

		redirectAttributes.addFlashAttribute("mensagem", "Posição cadastrada com sucesso.");
		return new ModelAndView("redirect:/admin/posicao");
	}

	@RequestMapping(method = RequestMethod.GET, value = "posicoes")
	@Cacheable("posicoes")
	public ModelAndView list() {
		ModelAndView view = new ModelAndView("posicao/list-posicao");
		
		view.addObject("posicoes", posicaoDAO.listarTodos());
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "posicao/{id}")
	public ModelAndView carregaPosicao(@PathVariable("id") Integer id) {
		return new ModelAndView("posicao/update-posicao").addObject("posicao",
				posicaoDAO.buscaPorId(id));
	}

	@RequestMapping(method = RequestMethod.POST, value = "posicao/{id}")
	@CacheEvict(value = "posicoes", allEntries = true)
	public ModelAndView update(@PathVariable("id") Integer id, @Valid Posicao posicao, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return new ModelAndView("posicao/update-posicao").addObject("posicao", posicao);
		}

		posicaoDAO.atualizaPosicao(posicao);
		redirectAttributes.addFlashAttribute("mensagem",
				"Posição "+posicao.getDescricao() +" atualizado com sucesso.");

		return new ModelAndView("redirect:/admin/posicoes");
	}

	@RequestMapping(method = RequestMethod.GET, value = "posicao/excluir/{id}")
	@CacheEvict(value = "posicoes", allEntries = true)
	public String remove(@PathVariable("id") Integer id) {
		Posicao posicao = posicaoDAO.buscaPorId(id);
		posicaoDAO.excluir(posicao);
		return "redirect:/admin/posicoes";
	}

}
