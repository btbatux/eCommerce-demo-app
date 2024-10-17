package com.btbatux.dream_shops.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalAmount = BigDecimal.ZERO; //sepetin toplam tutarı


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private Set<CartItem> cartItems; //sepetteki itemlar





    public void addItem(CartItem item) {
        this.cartItems.add(item);
        item.setCart(this);
        updateTotalAmount();
    }


    public void removeItem(CartItem item) {
        // 1. Verilen CartItem'ı sepetten (cartItems) kaldır.
        this.cartItems.remove(item);

        // 2. Kaldırılan CartItem ile sepet arasındaki ilişkiyi kopar (CartItem'ın sepetini null yap).
        item.setCart(null);

        // 3. Sepetin toplam tutarını güncelle.
        updateTotalAmount();
    }



    private void updateTotalAmount() {
        // Sepetteki tüm CartItem'ların toplam tutarını hesapla.
        this.totalAmount = cartItems.stream().map(item -> {
                    // Eğer birim fiyat null ise, BigDecimal.ZERO döndür.
                    BigDecimal unitPrice = item.getUnitPrice();
                    if (unitPrice == null) {
                        return BigDecimal.ZERO;
                    }

                    // Birim fiyat ile miktarı çarp ve o item'in toplam fiyatını hesapla.
                    return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                // Her bir CartItem'ın fiyatını topla ve toplam tutarı (totalAmount) elde et.
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
