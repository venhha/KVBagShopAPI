package com.example.kvbagshopapi.controllers;

import com.example.kvbagshopapi.dtos.*;
import com.example.kvbagshopapi.entities.Order;
import com.example.kvbagshopapi.entities.OrderItem;
import com.example.kvbagshopapi.entities.Product;
import com.example.kvbagshopapi.services.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Order")
public class OrderController {

    @Autowired
    IOrderService iOrderService;

    @Autowired
    ICartService iCartService;

    @Autowired
    IUserService iUserService;

    @Autowired
    IOrderItemService iOrderItemService;
    @Autowired
    IProductService iProductService;
    @Autowired
    IMailService iMailService;
    final ModelMapper modelMapper;

    public OrderController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping("/id")
    public ResponseEntity<List<OrderDto>> getAllCart(Long userId) {
        List<OrderDto> orderDtos = iOrderService.getAllOrder(userId);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    /***]
     * @author : Tu
     * @param userId : Truyền vào id của người đặt
     * @param address : Truyền vào địa chỉ
     * @param phoneNumber: Truyền vào số điện thoại
     * @param idCarts: Truyền vào list<Long> id của các cart muốn order
     *               , nếu k truyền thì mặc định là order tất cả trong cart
     * @return : Thông tin về order mơi
     */
    @PostMapping("/add-all")
    public ResponseEntity<OrderDto> addToOrder(@RequestParam("userId") Long userId,
                                               @RequestParam("address") String address,
                                               @RequestParam("phoneNumber") String phoneNumber,
                                               @RequestParam(value = "idCarts", required = false, defaultValue = "") List<Long> idCarts) {
        OrderDto orderDto = new OrderDto();
        int total = 0;
        List<CartDto> cartDtos = new ArrayList<>();
        if (idCarts.isEmpty()) {
            System.out.println("List empty");
            cartDtos = iCartService.getAllCartByUserId(userId);
        } else {
            System.out.println("List" + idCarts);
            for (Long id : idCarts
            ) {
                CartDto cartDto = iCartService.getCartById(id);
                if (cartDto != null) {
                    cartDtos.add(cartDto);
                }
            }
        }

//        for (CartDto cartDto: cartDtos
//             ) {
//            total += cartDto.getProduct().getPrice()*cartDto.getQuantity();
//        }

        UserDto userDto = iUserService.getUserById(userId);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();

        orderDto.setUser(userDto);
        orderDto.setTotal(total);
        orderDto.setAddress(address);
//        orderDto.setOrderItems(orderItemDtos);
        orderDto.setPhoneNumber(phoneNumber);


        OrderDto newOrder = iOrderService.newOrder(orderDto);
        int flag = 0;
            for (CartDto cartDto : cartDtos) {
                ProductDto productDto = iProductService.getProductById(cartDto.getProduct().getId());
                if (cartDto.getQuantity() <= productDto.getQuantity()) {
                    total += cartDto.getQuantity() * productDto.getPrice();

                    OrderItem newOne = modelMapper.map(cartDto, OrderItem.class);
                    newOne.setOrder(modelMapper.map(newOrder, Order.class));
                    newOne.setProduct(modelMapper.map(productDto, Product.class));
                    OrderItemDto newNewOne = iOrderItemService.addOrderItem(newOne);
                    orderItemDtos.add(newNewOne);


                    productDto.setQuantity(productDto.getQuantity() - cartDto.getQuantity());
                    productDto.setSold(productDto.getSold() + cartDto.getQuantity());

                    iProductService.updateProduct(productDto.getId(), productDto);

                    iCartService.deleteCart(cartDto.getId());

                    flag++;
                }
            }
            newOrder.setTotal(total);

            iOrderService.newOrder(newOrder);
            Order order = new Order();
            order.setId(newOrder.getId());
            if (flag == 0) {
                iOrderService.deleteOrder(order);
                return new ResponseEntity<>(newOrder, HttpStatus.NOT_ACCEPTABLE);
            }
        iMailService.sendOrderMail(userDto, newOrder,orderItemDtos);
        return new ResponseEntity<>(newOrder, HttpStatus.OK);
    }



}
