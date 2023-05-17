package com.example.kvbagshopapi.services.impl;

import com.example.kvbagshopapi.dtos.CartDto;
import com.example.kvbagshopapi.entities.Cart;
import com.example.kvbagshopapi.entities.User;
import com.example.kvbagshopapi.exceptions.NotFoundException;
import com.example.kvbagshopapi.repositories.CartRepository;
import com.example.kvbagshopapi.services.ICartService;
import com.example.kvbagshopapi.services.IProductService;
import com.example.kvbagshopapi.services.IUserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartServiceImpl implements ICartService {
    private final CartRepository cartRepository;
    private final IUserService iUserService;
    private final IProductService iProductService;
    private ModelMapper modelMapper;

    @Override
    public List<CartDto> getAllCartByUserId(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        return carts.stream()
                .map((cart) -> modelMapper.map(cart, CartDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CartDto getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null)
            throw new NotFoundException("Cant find category!");
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto addCart(CartDto cartDto, Long userId) {
        List<CartDto> cartDtos = getAllCartByUserId(userId);
        int flag = cartDto.getQuantity();
        for(CartDto cart : cartDtos){
            if(cart.getProduct().getId() == cartDto.getProduct().getId()){
                cartDto = cart;
                cartDto.setQuantity(flag + cartDto.getQuantity());
                break;
            }
        }
        Cart cart = modelMapper.map(cartDto, Cart.class);
        cart.setUser(modelMapper.map(iUserService.getUserById(userId), User.class));
        CartDto savedCardDto = modelMapper.map(cartRepository.save(cart), CartDto.class);
        return savedCardDto;
    }

    @Override
    public CartDto updateCart(Long cartId, int quantity) {
//        Cart cart = modelMapper.map(cartDto, Cart.class);
//        CartDto updatedCardDto = modelMapper.map(cartRepository.save(cart), CartDto.class);
//        return updatedCardDto;
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) throw new NotFoundException("Cant find category!");

        cart.setQuantity(quantity);
        CartDto cartDto = modelMapper.map(cartRepository.save(cart), CartDto.class);
        return cartDto;
    }

    @Override
    public int countAllCartByUserId(Long userId) {
        return cartRepository.countByUserId(userId);
    }

    @Override
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
