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

import br.com.cc.varzeafc.daos.PatrocionadorDAO;
import br.com.cc.varzeafc.models.Patrocinador;

@Controller
@Transactional
@Scope(value=WebApplicationContext.SCOPE_REQUEST)
@RequestMapping("/admin")
public class AdminPatrocionadorController {

	@Autowired
	private PatrocionadorDAO patrocinadorDAO;

	@RequestMapping(method = RequestMethod.GET, value = "patrocinador")
	public ModelAndView form(Patrocinador patrocinador) {
		return new ModelAndView("patrocinador/add-patrocinador");
	}

	@RequestMapping(method = RequestMethod.POST, value = "patrocinador")
	@CacheEvict(value = "patrocinadores", allEntries = true)
	public ModelAndView add(@Valid Patrocinador patrocinador, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return form(patrocinador);

		}

		patrocinadorDAO.salva(patrocinador);

		redirectAttributes.addFlashAttribute("mensagem", "Patrocinador cadastrado com sucesso.");
		return new ModelAndView("redirect:/admin/patrocinador");
	}

	@RequestMapping(method = RequestMethod.GET, value = "patrocinadores")
	@Cacheable("patrocinadores")
	public ModelAndView list() {
		ModelAndView view = new ModelAndView("patrocinador/list-patrocinador");
		view.addObject("patrocinadores", patrocinadorDAO.listarTodos());
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "patrocinador/{id}")
	public ModelAndView carregaPatrocinador(@PathVariable("id") Integer id) {
		return new ModelAndView("patrocinador/update-patrocinador").addObject("patrocinador",
				patrocinadorDAO.buscaPorId(id));
	}

	@RequestMapping(method = RequestMethod.POST, value = "patrocinador/{id}")
	@CacheEvict(value = "patrocinadores", allEntries = true)
	public ModelAndView update(@PathVariable("id") Integer id, @Valid Patrocinador patrocinador, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return new ModelAndView("patrocinador/update-patrocinador").addObject("patrocinador", patrocinador);
		}

		patrocinadorDAO.atualizaPatrocinador(patrocinador);
		redirectAttributes.addFlashAttribute("mensagem",
				"Patrocinador "+patrocinador.getNome() +" atualizado com sucesso.");

		return new ModelAndView("redirect:/admin/patrocinadores");
	}

	@RequestMapping(method = RequestMethod.GET, value = "patrocinador/excluir/{id}")
	@CacheEvict(value = "patrocinadores", allEntries = true)
	public String remove(@PathVariable("id") Integer id) {
		Patrocinador patrocinador = patrocinadorDAO.buscaPorId(id);
		patrocinadorDAO.excluir(patrocinador);
		return "redirect:/admin/patrocinadores";
	}

}
