package com.shopme.common.entity;

import java.beans.Transient;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_track")
public class OrderTrack  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 256)
	private String notes;

	private Date updatedTime;

	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Transient
	public String getUpdatedTimeOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		//dateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Istanbul"));
		return dateFormatter.format(this.updatedTime);
	}
	
	public void setUpdatedTimeOnForm(String dateString) throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
		//dateFormatter.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Istanbul"));
		try {
			this.updatedTime = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}
	
}
