package com.btbatux.dream_shops.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart; //Sepette birden fazla ürün olabilir

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //bir ürünün birden çok item'a sahip olur



    public void setTotalPrice() {
        this.totalPrice = product.getPrice().multiply(new BigDecimal(quantity));
    }
}
