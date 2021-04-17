package com.pasarela.entity;

public class Producto {
	private String productName;
	private float subtotal;
	private float shipping;
	private float tax;
	private float total;
	
	public Producto(String productName, String subtotal, String shipping, String tax, String total) {
		super();
		this.productName = productName;
		this.subtotal = Float.parseFloat(subtotal);
		this.shipping = Float.parseFloat(shipping);
		this.tax = Float.parseFloat(tax);
		this.total = Float.parseFloat(total);
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getSubtotal() {
		return String.format("%.3f", subtotal);
	}

	public String getShipping() {
		return String.format("%.3f", shipping) ;
	}
	
	public String getTax() {
		return String.format("%.3f", tax);
	}

	public String getTotal() {
		return String.format("%.3f", total);
	}
}
