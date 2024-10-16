package com.btbatux.dream_shops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity; //miktar adet
    private BigDecimal unitPrice; //birim fiyat
    private BigDecimal totalPrice;  //toplam fiyat


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private Cart cart; //Sepette birden fazla ürün olabilir

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //bir ürünün birden çok item'a sahip olur


    public void totalPrice() {
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
