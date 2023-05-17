package com.example.kvbagshopapi.controllers;

import com.example.kvbagshopapi.dtos.CartDto;
import com.example.kvbagshopapi.dtos.ProductDto;
import com.example.kvbagshopapi.repositories.CartRepository;
import com.example.kvbagshopapi.services.ICartService;
import com.example.kvbagshopapi.services.IProductService;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Cart")
public class CartController {
    @Autowired
    ICartService iCartService;
    @Autowired
    IProductService iproductService;
    @Autowired
    CartRepository cartRepository;

    @GetMapping("/user/{id}")
    @ApiOperation(value = "Get all Cart of User")
    public ResponseEntity<List<CartDto>> getAllCart_1(@PathVariable("id") Long userId) {
        List<CartDto> listCart = iCartService.getAllCartByUserId(userId);
        if (listCart.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listCart, HttpStatus.OK);
    }
    @GetMapping("/user")
    @ApiOperation(value = "Get all Cart of User")
    public ResponseEntity<List<CartDto>> getAllCart(Long userId) {
        List<CartDto> listCart = iCartService.getAllCartByUserId(userId);
        if (listCart.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listCart, HttpStatus.OK);
    }

    @ApiOperation(value = "Add cart")
    @PostMapping
    public ResponseEntity<CartDto> createCart(Long productId, Long userId, int quantity) {
        ProductDto productDto = iproductService.getProductById(productId);
        CartDto newCartDto = new CartDto();
        newCartDto.setQuantity(quantity);
        newCartDto.setProduct(productDto);
        CartDto savedCartDto = iCartService.addCart(newCartDto, userId);
        return new ResponseEntity<>(savedCartDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Cart (Update quantity of product in cart)")
    @PutMapping("/update")
    public ResponseEntity<CartDto> updateCart(Long cartId, int quantity) {
        CartDto newCartDto = iCartService.updateCart(cartId,quantity);
        System.out.println(cartId+ "---"+ quantity);
        return new ResponseEntity<>(newCartDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Deleted Cart (Deleted quantity of product in cart)")
    @DeleteMapping("")
    public ResponseEntity<String> deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
        System.out.println(cartId);
        String msg = "Deleted successfully!!";
//        return new ResponseEntity<>(msg, HttpStatus.OK);
        final Gson gson = new Gson();
        return new ResponseEntity<>(gson.toJson(msg), HttpStatus.OK);
    }

    @ApiOperation(value = "Lấy sổ lượng cart của user")
    @GetMapping("/count/{userId}")
    public ResponseEntity<Integer> countAllCart(Long userId){
        return new ResponseEntity<>(iCartService.countAllCartByUserId(userId), HttpStatus.OK);
    }

}
