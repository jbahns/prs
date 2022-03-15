package com.maxtrain.prs.requestline;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.maxtrain.prs.product.Product;
import com.maxtrain.prs.request.Request;

@Entity(name="requestlines")
public class Requestline {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int quantity;
	
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="requestId")
	private Request request;
	
	//@JsonBackReference(value="lineprod")
	@ManyToOne(optional=false)
	@JoinColumn(name="productId")
	private Product product;
	
	public Requestline() {}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}


	public Product getProduct() {
		return product;
	}


	public void setProduct(Product product) {
		this.product = product;
	}



	
	
}
