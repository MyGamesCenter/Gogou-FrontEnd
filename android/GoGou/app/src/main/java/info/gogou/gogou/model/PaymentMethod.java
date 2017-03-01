package info.gogou.gogou.model;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private PaymentType paymentType;

	private String paymentId;
	
	private String paymentToken;
	
	private String payerName;

	private String userName;

	private String cardCvv;

	private String cardExpires;

	private String cardNumber;

	private String cardOwner;

	private CreditCardType cardType;
	
	private Address address;
	
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public String getPayerName() {
		return payerName;
	}

	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCardCvv() {
		return cardCvv;
	}

	public String getCardExpires() {
		return cardExpires;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getCardOwner() {
		return cardOwner;
	}

	public CreditCardType getCardType() {
		return cardType;
	}


	public void setCardCvv(String cardCvv) {
		this.cardCvv = cardCvv;
	}

	public void setCardExpires(String cardExpires) {
		this.cardExpires = cardExpires;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}

	public void setCardType(CreditCardType cardType) {
		this.cardType = cardType;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}
