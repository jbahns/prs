package com.maxtrain.prs.request;

import java.util.List;

import javax.persistence.*;



import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.maxtrain.prs.user.User;
import com.maxtrain.prs.requestline.Requestline;


@Entity(name="requests")
public class Request {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(length=80, nullable = false)
	private String description;

	@Column(length=80, nullable = false)
	private String justification;

	@Column(length=80, nullable = false)
	private String rejectionReason;

	@Column(length=20, nullable = false)
	private String deliveryMode;

	@Column(length=10, nullable = false)
	private String status;
	
	@Column(columnDefinition="decimal(11,2) NOT NULL DEFAULT 0.0")
	private double total;
	
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="userId")
	private User user;

	@JsonManagedReference
	@OneToMany(mappedBy="request")
	private List<Requestline> requestlines;
	
	
	public Request() {}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public String getDeliveryMode() {
		return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Requestline> getRequestlines() {
		return requestlines;
	}

	public void setRequestlines(List<Requestline> requestlines) {
		this.requestlines = requestlines;
	}
	
}