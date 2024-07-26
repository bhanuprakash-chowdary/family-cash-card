package com.example.cashcard;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class CashCard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@JsonProperty("id")
    public Long id;

    @JsonProperty("amount")
    public Double amount;

    @JsonProperty("owner")
    public String owner;

    public CashCard() {
    }

    public CashCard(Long id, Double amount, String owner) {
        this.id = id;
        this.amount = amount;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashCard cashCard = (CashCard) o;
        return Objects.equals(id, cashCard.id) &&
                Objects.equals(amount, cashCard.amount) &&
                Objects.equals(owner, cashCard.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, owner);
    }
}

