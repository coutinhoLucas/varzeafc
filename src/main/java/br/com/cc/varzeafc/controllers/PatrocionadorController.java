package br.com.cc.varzeafc.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.cc.varzeafc.daos.PatrocionadorDAO;
import br.com.cc.varzeafc.models.Patrocinador;

@Controller
@Transactional
public class PatrocionadorController {

	@Autowired
	private PatrocionadorDAO patrocinadorDAO;

	@RequestMapping(method = RequestMethod.GET, value = "/patrocinador")
	public ModelAndView form(Patrocinador patrocinador) {
		return new ModelAndView("patrocinador/add-patrocinador");
	}

	@RequestMapping(method = RequestMethod.POST, value = "/patrocinador")
	@CacheEvict(value = "patrocinadores", allEntries = true)
	public ModelAndView add(@Valid Patrocinador patrocinador, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return form(patrocinador);

		}

		patrocinadorDAO.salva(patrocinador);

		redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso.");
		return new ModelAndView("redirect:patrocinador");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/patrocinadores")
	@Cacheable("patrocinadores")
	public ModelAndView list() {
		ModelAndView view = new ModelAndView("patrocinador/list-patrocinador");
		view.addObject("patrocinadores", patrocinadorDAO.listarTodos());
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/patrocinador/{id}", produces = "application/json")
	public @ResponseBody String carregaPatrocinador(@PathVariable("id") Integer id) {
		Patrocinador patrocinador = patrocinadorDAO.buscaPorId(id);
		return patrocinador.toJson();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/patrocinador/{id}")
	@CacheEvict(value = "patrocinadores", allEntries = true)
	public String update(Patrocinador patrocinador, final BindingResult bindingResult) {
		patrocinadorDAO.atualizaPatrocinador(patrocinador);
		return "redirect:/patrocinadores";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/patrocinador/excluir/{id}")
	@CacheEvict(value = "patrocinadores", allEntries = true)
	public String remove(@PathVariable("id") Integer id) {
		Patrocinador patrocinador = patrocinadorDAO.buscaPorId(id);
		patrocinadorDAO.excluir(patrocinador);
		return "redirect:/patrocinadores";
	}

}
