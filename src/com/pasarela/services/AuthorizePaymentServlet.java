package com.pasarela.services;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pasarela.action.PaymentServices;
import com.pasarela.entity.Producto;
import com.paypal.base.rest.PayPalRESTException;


@WebServlet("/autorizar_pago")
public class AuthorizePaymentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    
    public AuthorizePaymentServlet() {
        
    }

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String productName = request.getParameter("productName");
		String subtotal = request.getParameter("subtotal");
		String shipping = request.getParameter("shipping");
		String tax = request.getParameter("tax");
		String total = request.getParameter("total");
		
		Producto producto = new Producto(productName, subtotal, shipping, tax, total);
		
		try {
			PaymentServices paymentServices = new PaymentServices();
			String approvalLink = paymentServices.authorizePayment(producto);
			
			response.sendRedirect(approvalLink);
		} catch (PayPalRESTException ex) {
			ex.printStackTrace();
			request.setAttribute("errorMessage", "Detalles en el pago no válidos");
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
		}
	}

}
