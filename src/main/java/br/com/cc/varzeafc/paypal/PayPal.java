package br.com.cc.varzeafc.paypal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import br.com.cc.varzeafc.util.PayPalConstants;

@Repository
public class PayPal {
	
	public Payment createPayment(HttpServletRequest req, HttpServletResponse resp, String valor) throws Exception {
		Payment createdPayment = null;

		APIContext apiContext = new APIContext(PayPalConstants.clientID, PayPalConstants.clientSecret,
				PayPalConstants.mode);

		if (req.getParameter("PayerID") != null) {

			Payment payment = new Payment();

			if (req.getParameter("paymentId") != null) {

				payment.setId(req.getParameter("paymentId"));

			}

			PaymentExecution paymentExecution = new PaymentExecution();

			paymentExecution.setPayerId(req.getParameter("PayerID"));

			try {

				createdPayment = payment.execute(apiContext, paymentExecution);
				System.out.println(
						"Executed The Payment " + Payment.getLastRequest() + "   " + Payment.getLastResponse());

			} catch (PayPalRESTException e) {
				throw new Exception();
			}
		} else {

			Details details = new Details();
			details.setShipping("0");
			details.setSubtotal(valor);
			details.setTax("0");

			Amount amount = new Amount();
			amount.setCurrency("BRL");
			amount.setTotal(valor);
			amount.setDetails(details);

			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setDescription("This is the payment transaction description.");

			Item item = new Item();
			item.setName("varzeafc").setQuantity("1").setCurrency("BRL").setPrice(valor);
			ItemList itemList = new ItemList();
			List<Item> items = new ArrayList<Item>();
			items.add(item);
			itemList.setItems(items);

			transaction.setItemList(itemList);

			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");

			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);

			RedirectUrls redirectUrls = new RedirectUrls();
			redirectUrls.setCancelUrl(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
					+ req.getContextPath() + "/varzeafc/paymentwithpaypalcancel");
			redirectUrls.setReturnUrl(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
					+ req.getContextPath() + "/varzeafc/paymentwithpaypalprocess");
			payment.setRedirectUrls(redirectUrls);

			try {
				createdPayment = payment.create(apiContext);
				System.out.println("Created payment with id = " + createdPayment.getId() + " and status = "
						+ createdPayment.getState());
				Iterator<Links> links = createdPayment.getLinks().iterator();
				
				while (links.hasNext()) {
					Links link = links.next();
					if (link.getRel().equalsIgnoreCase("approval_url")) {
						req.setAttribute("redirectURL", link.getHref());
					}
				}
				System.out
						.println("Payment with PayPal " + Payment.getLastRequest() + "  " + Payment.getLastResponse());

			} catch (PayPalRESTException e) {
				throw new Exception();
			}
		}
		return createdPayment;
	}

}