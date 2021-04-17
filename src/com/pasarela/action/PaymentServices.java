package com.pasarela.action;

import java.util.ArrayList;
import java.util.List;

import com.pasarela.entity.Producto;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

public class PaymentServices {
	private static final String CLIENT_ID = "AfIeagoI1MY5xZH1PXGXbGoZa_wYwRPQjGp45JCxcuqrOvyFYHl_-y3SoySHCdWlPWy1qKKUnVjicmnb";
	private static final String CLIENT_SECRET = "EE9t7npHlHrLAhrTSCk4_ri-2SFIv5SGXa-yh3_wJNndiBIUQYwElM9Pn9AgXH42y7EgNnGJ1XHEEylB";
	private static final String MODE = "sandbox";
	
	public String authorizePayment (Producto producto) throws PayPalRESTException {
		Payer payer = getPayerInformation();
		RedirectUrls redirectUrls = getRedirectURLs();
		List<Transaction> listTransaction = getTransactionInformation(producto);
		
		Payment requestPayment = new Payment();
		requestPayment.setTransactions(listTransaction)
						.setRedirectUrls(redirectUrls)
						.setPayer(payer)
						.setIntent("authorize");
		
		APIContext apiContext = new APIContext (CLIENT_ID, CLIENT_SECRET, MODE);
		Payment approvedPayment = requestPayment.create(apiContext);
		
		System.out.println(approvedPayment);
						
		return getApprovalLink(approvedPayment);
	}
	
	private String getApprovalLink(Payment approvedPayment) {
	    List<Links> links = approvedPayment.getLinks();
	    String approvalLink = null;
	     
	    for (Links link : links) {
	        if (link.getRel().equalsIgnoreCase("approval_url")) {
	            approvalLink = link.getHref();
	            break;
	        }
	    }      
	     
	    return approvalLink;
	}
	
	private List<Transaction> getTransactionInformation(Producto orderDetail) {
	    Details details = new Details();
	    details.setShipping(orderDetail.getShipping());
	    details.setSubtotal(orderDetail.getSubtotal());
	    details.setTax(orderDetail.getTax());
	 
	    Amount amount = new Amount();
	    amount.setCurrency("USD");
	    amount.setTotal(orderDetail.getTotal());
	    amount.setDetails(details);
	 
	    Transaction transaction = new Transaction();
	    transaction.setAmount(amount);
	    transaction.setDescription(orderDetail.getProductName());
	     
	    ItemList itemList = new ItemList();
	    List<Item> items = new ArrayList<>();
	     
	    Item item = new Item();
	    item.setCurrency("USD");
	    item.setName(orderDetail.getProductName());
	    item.setPrice(orderDetail.getSubtotal());
	    item.setTax(orderDetail.getTax());
	    item.setQuantity("1");
	     
	    items.add(item);
	    itemList.setItems(items);
	    transaction.setItemList(itemList);
	 
	    List<Transaction> listTransaction = new ArrayList<>();
	    listTransaction.add(transaction);  
	     
	    return listTransaction;
	}

	private RedirectUrls getRedirectURLs() {
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://localhost:8080/PasarelaDePago/cancelar.html");
		redirectUrls.setReturnUrl("http://localhost:8080/PasarelaDePago/revisar-pago");
		
		return redirectUrls;
	}
	
	public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
	    APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
	    return Payment.get(apiContext, paymentId);
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
	    PaymentExecution paymentExecution = new PaymentExecution();
	    paymentExecution.setPayerId(payerId);
	 
	    Payment payment = new Payment().setId(paymentId);
	 
	    APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
	 
	    return payment.execute(apiContext, paymentExecution);
	}

	private Payer getPayerInformation() {
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		
		PayerInfo payerInfo = new PayerInfo();
		payerInfo.setFirstName("John")
			.setLastName("Doe")
			.setEmail("sb-a6ymg5937234@personal.example.com");
		
		payer.setPayerInfo(payerInfo);
		return payer;
	}
	
}
