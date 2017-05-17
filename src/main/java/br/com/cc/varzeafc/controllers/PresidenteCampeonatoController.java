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
@Scope(value = WebApplicationContext.SCOPE_SESSION)
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
	public ModelAndView inscricaoCampeonato(@PathVariable("id") Integer id, Campeonato campeonato,
			HttpSession session) {
		campeonato = campeonatoDAO.buscaPorId(id);
		session.setAttribute("taxaInscricao", campeonato.getTaxaDeInscricao());
		return new ModelAndView("campeonato/inscricao-campeonato").addObject("campeonato", campeonato);
	}

	@RequestMapping(method = RequestMethod.POST, value = "pagamento")
	public String processaPagamento(Inscricao inscricao, HttpServletRequest req, HttpServletResponse resp) {

		try {

			Double valorInscricao = (Double) req.getSession().getAttribute("taxaInscricao");
			Payment payment = payPal.createPayment(req, resp, Double.toString(valorInscricao));
			inscricao.salvaDadosInscricao(payment, inscricaoDAO, equipeDAO, valorInscricao);

		} catch (Exception e) {
			return "redirect:erropagamento";
		}

		return "redirect:" + req.getAttribute("redirectURL").toString();
	}

	@RequestMapping(method = RequestMethod.GET, value = "paymentwithpaypalprocess")
	public ModelAndView payPalProcess(HttpServletRequest req, HttpServletResponse resp) {

		try {

			Payment payment = payPal.createPayment(req, resp, null);
			Inscricao inscricao = inscricaoDAO.getInscricaoPeloCodigoPagamento(payment.getId());
			inscricao.setStatusPagamento(payment.getState());
			inscricaoDAO.update(inscricao);

		} catch (Exception e) {
			return new ModelAndView("redirect:erropagamento");
		}

		return new ModelAndView("redirect:pagamentook");

	}

	@RequestMapping(method = RequestMethod.GET, value = "paymentwithpaypalcancel")
	public String payPalCancel(HttpServletRequest req, HttpServletResponse resp) {
		try {

			Payment payment = payPal.createPayment(req, resp, null);

		} catch (Exception e) {
			return "erros/erro";
		}

		return "erros/erro";
	}

	@RequestMapping(method = RequestMethod.GET, value = "pagamentook")
	public ModelAndView pagamentoOk() {
		return new ModelAndView("pagamento/success");
	}

	@RequestMapping(method = RequestMethod.GET, value = "erropagamento")
	public ModelAndView erroPagamento() {
		return new ModelAndView("erros/erro");
	}

}
