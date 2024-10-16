package com.btbatux.dream_shops.service.cart;

import com.btbatux.dream_shops.model.Cart;
import com.btbatux.dream_shops.model.CartItem;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.repository.CartItemRepository;
import com.btbatux.dream_shops.repository.CartRepository;
import com.btbatux.dream_shops.service.product.IProductService;
import org.springframework.stereotype.Service;

@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final IProductService iProductService;
    private final CartRepository cartRepository;

    public CartItemService(CartItemRepository cartItemRepository,
                           CartService cartService,
                           IProductService iProductService,
                           CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.iProductService = iProductService;

    }


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Sepeti al: Verilen cartId'ye göre mevcut sepeti (Cart) getirir.
        Cart cart = cartService.getCart(cartId);

        // 2. Ürünü al: Verilen productId'ye göre mevcut ürünü (Product) getirir.
        Product product = iProductService.getProductById(productId);

        // 3. Sepette aynı üründen daha önce eklenmiş mi kontrol et:
        // Eğer ürün zaten sepette varsa (CartItem) o ürünü alır, yoksa yeni bir CartItem oluşturur.
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))  // Sepette aynı ürünü arar.
                .findFirst()
                .orElse(new CartItem()); // Eğer ürün bulunamazsa yeni bir CartItem oluşturur.

        // 4. Eğer CartItem yeni oluşturulduysa (ilk defa ekleniyorsa):
        if (cartItem.getId() == null) {
            // CartItem'ın bilgilerini set et (cart, product, quantity, unitPrice).
            cartItem.setCart(cart); // Bu CartItem'ın ait olduğu sepeti set eder.
            cartItem.setProduct(product); // Bu CartItem'ın ait olduğu ürünü set eder.
            cartItem.setQuantity(quantity); // Miktar olarak verilen değeri set eder.
            cartItem.setUnitPrice(product.getPrice()); // Ürünün birim fiyatını set eder.
        } else {
            // Eğer ürün daha önce sepete eklenmişse, miktarı artır.
            cartItem.setQuantity(cartItem.getQuantity() + quantity); // Mevcut miktara yeni miktarı ekler.
        }

        // 5. Toplam fiyatı hesapla ve set et (birim fiyat * miktar).
        cartItem.setTotalPrice();

        // 6. Sepete (Cart) bu CartItem'ı ekle.
        cart.addItem(cartItem);

        // 7. CartItem ve Cart veritabanına kaydedilir.
        cartItemRepository.save(cartItem); // CartItem kaydedilir.
        cartRepository.save(cart); // Sepet (Cart) kaydedilir.
    }



    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

    }



    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {

    }
}
