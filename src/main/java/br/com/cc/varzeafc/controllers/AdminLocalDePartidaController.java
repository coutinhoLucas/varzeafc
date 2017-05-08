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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.cc.varzeafc.daos.EnderecoDAO;
import br.com.cc.varzeafc.daos.LocalDePartidaDAO;
import br.com.cc.varzeafc.models.LocalPartida;

@Controller
@Transactional
@Scope("session")
@RequestMapping("/admin")
public class AdminLocalDePartidaController {

	@Autowired
	private LocalDePartidaDAO localDePartidaDAO;
	
	@Autowired
	private EnderecoDAO enderecoDAO;

	@RequestMapping(method = RequestMethod.GET, value = "localPartida")
	public ModelAndView form(LocalPartida localPartida) {
		return new ModelAndView("localPartida/add-localPartida");
	}

	@RequestMapping(method = RequestMethod.POST, value = "localPartida")
	@CacheEvict(value = "locaisPartida", allEntries = true)
	public ModelAndView add(@Valid LocalPartida localPartida, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return form(localPartida);

		}
		
		enderecoDAO.salva(localPartida.getEndereco());
		localDePartidaDAO.salva(localPartida);

		redirectAttributes.addFlashAttribute("mensagem", "Local de Partida cadastrado com sucesso.");
		return new ModelAndView("redirect:/admin/localPartida");
	}

	@RequestMapping(method = RequestMethod.GET, value = "locaisPartida")
	@Cacheable("locaisPartida")
	public ModelAndView list() {
		ModelAndView view = new ModelAndView("localPartida/list-localPartida");
		view.addObject("locaisPartidas", localDePartidaDAO.listarTodos());
		return view;
	}

	@RequestMapping(method = RequestMethod.GET, value = "localPartida/{id}")
	public ModelAndView carregaLocalPartida(@PathVariable("id") Integer id) {
		return new ModelAndView("localPartida/update-localPartida").addObject("localPartida",
				localDePartidaDAO.buscaPorId(id));
	}

	@RequestMapping(method = RequestMethod.POST, value = "localPartida/{id}")
	@CacheEvict(value = "locaisPartida", allEntries = true)
	public ModelAndView update(@PathVariable("id") Integer id, @Valid LocalPartida localPartida, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return new ModelAndView("localPartida/update-localPartida").addObject("localPartida", localPartida);
		}

		localDePartidaDAO.atualizaLocalPartida(localPartida);
		redirectAttributes.addFlashAttribute("mensagem",
				"Local de Partida "+localPartida.getNomeLocalPartida() +" atualizado com sucesso.");

		return new ModelAndView("redirect:/admin/locaisPartida");
	}

	@RequestMapping(method = RequestMethod.GET, value = "localPartida/excluir/{id}")
	@CacheEvict(value = "locaisPartida", allEntries = true)
	public String remove(@PathVariable("id") Integer id) {
		LocalPartida localPartida = localDePartidaDAO.buscaPorId(id);
		localDePartidaDAO.excluir(localPartida);
		return "redirect:/admin/locaisPartida";
	}

}
