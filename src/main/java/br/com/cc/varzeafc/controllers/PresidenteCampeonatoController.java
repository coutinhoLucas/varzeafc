package br.com.cc.varzeafc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.paypal.api.payments.Payment;

import br.com.cc.varzeafc.daos.CampeonatoDAO;
import br.com.cc.varzeafc.daos.EquipeDAO;
import br.com.cc.varzeafc.daos.InscricaoDAO;
import br.com.cc.varzeafc.models.Campeonato;
import br.com.cc.varzeafc.models.Inscricao;
import br.com.cc.varzeafc.paypal.PayPal;



@Controller
@RequestMapping("/varzeafc")
@Transactional
@Scope(value=WebApplicationContext.SCOPE_SESSION)
public class PresidenteCampeonatoController {

	@Autowired
	private CampeonatoDAO campeonatoDAO;

	@Autowired
	private EquipeDAO equipeDAO;

	@Autowired
	private InscricaoDAO inscricaoDAO;

	@Autowired
	private PayPal payPal;

	@RequestMapping(method = RequestMethod.GET, value = "campeonatos")
	public ModelAndView shopCampeonatos(Campeonato campeonato) {
		return new ModelAndView("campeonato/loja-campeonatos").addObject("campeonatos",
				campeonatoDAO.listaCampeonatosAbertos());
	}

	@RequestMapping(method = RequestMethod.GET, value = "campeonato/{id}")
	public ModelAndView inscricaoCampeonato(@PathVariable("id") Integer id, Campeonato campeonato, HttpSession session) {
		campeonato = campeonatoDAO.buscaPorId(id);
		session.setAttribute("taxaInscricao", campeonato.getTaxaDeInscricao());
		return new ModelAndView("campeonato/inscricao-campeonato").addObject("campeonato", campeonato);
	}


	@RequestMapping(method = RequestMethod.POST, value = "pagamento")
	public String processaPagamento(Inscricao inscricao, HttpServletRequest req, HttpServletResponse resp) {

		Payment payment = payPal.createPayment(req, resp, Double.toString((Double) req.getSession().getAttribute("taxaInscricao")));

		if (payment == null) {
			return "erros/erro";
		}

		inscricao.salvaDadosInscricao(payment, inscricaoDAO, equipeDAO);

		return "redirect:" + req.getAttribute("redirectURL").toString();
	}

	@RequestMapping(method = RequestMethod.GET, value = "paymentwithpaypalprocess")
	public String payPalProcess(HttpServletRequest req, HttpServletResponse resp) {
		Payment payment = payPal.createPayment(req, resp, null);

		if (payment == null) {
			System.out.println("Erro");
		}
		
		return "pagamento/success";
	}

	@RequestMapping(method = RequestMethod.GET, value = "paymentwithpaypalcancel")
	public String payPalCancel(HttpServletRequest req, HttpServletResponse resp) {
		Payment payment = payPal.createPayment(req, resp, null);

		if (payment == null) {
			System.out.println("Erro");
		}
		
		return "erros/erro";
	}

}
