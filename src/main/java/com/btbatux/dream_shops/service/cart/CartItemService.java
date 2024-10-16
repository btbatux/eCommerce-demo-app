package com.btbatux.dream_shops.service.cart;

import com.btbatux.dream_shops.model.Cart;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.repository.CartItemRepository;
import com.btbatux.dream_shops.service.product.IProductService;
import org.springframework.stereotype.Service;

@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final ICartService cartService;
    private final IProductService iProductService;

    public CartItemService(CartItemRepository cartItemRepository, CartService cartService, IProductService iProductService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.iProductService = iProductService;
    }


    @Override
    public void addItemToCart(Long cartId, Long productId, int quantity) {
        //1.Sepeti getir
        //2.Ürünü getir
        //3.Ürünün sepette olup olmadığığnı kontrol et
        //4.evet ise miktarı talep miktarıyla arttır
        //5.Eğer yok ise yeni bir sepet öğesi başlat
        Cart cart = cartService.getCart(cartId);
        Product product = iProductService.getProductById(productId);



    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {

    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {

    }
}
