package com.btbatux.dream_shops.service.cart;

import com.btbatux.dream_shops.dto.ProductDto;
import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.Cart;
import com.btbatux.dream_shops.model.CartItem;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.repository.CartItemRepository;
import com.btbatux.dream_shops.repository.CartRepository;
import com.btbatux.dream_shops.service.product.IProductService;
import com.btbatux.dream_shops.service.product.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public CartItemService(CartItemRepository cartItemRepository,
                           CartService cartService,
                           ProductService productService,
                           CartRepository cartRepository, ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        // 1. Sepeti al: Verilen cartId'ye göre mevcut sepeti (Cart) getirir.
        Cart cart = cartService.getCart(cartId);

        // 2. Ürünü al: Verilen productId'ye göre mevcut ürünü (Product) getirir.
        ProductDto productDto = productService.getProductById(productId);

        // 3. Sepette aynı üründen daha önce eklenmiş mi kontrol et:
        // Eğer ürün zaten sepette varsa (CartItem) o ürünü alır, yoksa yeni bir CartItem oluşturur.
        CartItem cartItem = cart
                .getCartItems().stream()
                .filter(item ->
                        item.getProduct().getId().equals(productDto.getId()))  // Sepette aynı ürünü arar.
                .findFirst()
                .orElse(new CartItem()); // Eğer ürün bulunamazsa yeni bir CartItem oluşturur.

        // 4. Eğer CartItem yeni oluşturulduysa (ilk defa ekleniyorsa):
        if (cartItem.getId() == null) {
            // CartItem'ın bilgilerini set et (cart, product, quantity, unitPrice).
            cartItem.setCart(cart); // Bu CartItem'ın ait olduğu sepeti set eder.
            cartItem.setProduct(modelMapper.map(productDto, Product.class)); // Bu CartItem'ın ait olduğu ürünü set eder.
            cartItem.setQuantity(quantity); // Miktar olarak verilen değeri set eder.
            cartItem.setUnitPrice(productDto.getPrice()); // Ürünün birim fiyatını set eder.
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
        //Sepeti al: Verilen cartId'ye göre mevcut sepeti (Cart) getirir.
        Cart cart = cartService.getCart(cartId);
        //Sepette belirtilen productId'ye sahip ürünün olup olmadığını kontrol et ve bul:
        //Sepette (CartItem) bu productId ile eşleşen ürünü bulur.

        CartItem itemRemove = getCartItem(cartId, productId);
        //Sepetten ürünü çıkar: Bulunan CartItem'ı sepetten çıkarır.
        cart.removeItem(itemRemove);
        //Güncellenen sepeti kaydet: Sepeti veritabanına kaydeder.
        cartRepository.save(cart);
    }


    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        // 1. Verilen sepet ID'sine göre sepeti al.
        Cart cart = cartService.getCart(cartId);

        // 2. Sepetteki ürünleri (cartItems) dolaş ve verilen ürün ID'sine (productId) sahip olanı bul.
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    // 3. Eğer ürün bulunduysa, bu ürünün miktarını (quantity) güncelle.
                    item.setQuantity(quantity);
                    // 4. Ürünün birim fiyatını güncelle (eğer fiyat değişmişse).
                    item.setUnitPrice(item.getProduct().getPrice());
                    // 5. Yeni miktara göre toplam fiyatı güncelle.
                    item.setTotalPrice();
                });
        // 6. Sepetteki tüm ürünlerin toplam fiyatını hesapla (totalAmount).
        BigDecimal totalAmount = cart.getCartItems()
                .stream()
                .map(CartItem::getTotalPrice)  // Her CartItem için toplam fiyatı al
                .reduce(BigDecimal.ZERO, BigDecimal::add);  // Toplamları birleştir
        // 7. Sepetin toplam tutarını (totalAmount) güncelle.
        cart.setTotalAmount(totalAmount);
        // 8. Sepeti veritabanına kaydet.
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId,
                                Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getCartItems().stream().filter(item ->
                        item.getProduct().
                                getId().equals(productId)).
                findFirst().
                orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));
    }

}
