package jpabook.jpashop.api;


import jpabook.jpashop.Repository.OrderRepository;
import jpabook.jpashop.Repository.OrderSearch;
import jpabook.jpashop.Repository.SimpleOrderQueryDto;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersv1(){
        List<Order> all=orderRepository.findAll(new OrderSearch());
        //강제 LAZY LOADING 강제로 꺼내니깐 PROXY가 실제 객체로 바뀜
        for(Order order:all){
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }
    @GetMapping("api/v2/simple-orders")
    public List<SimpleOrderDto>ordersv2(){
        List<Order>orders=orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto>result=orders.stream().map(SimpleOrderDto::new).collect(Collectors.toList());
        return result;
    }
    @GetMapping("api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders=orderRepository.findWithMemberDelivery();
        List<SimpleOrderDto> result=orders.stream().map(SimpleOrderDto::new).collect(Collectors.toList());
        return result;
    }
    @GetMapping("api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersv4(){
        return orderRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
    public SimpleOrderDto(Order order){
        orderId=order.getId();
        name=order.getMember().getName();
        orderDate=order.getOrderDate();
        orderStatus=order.getStatus();
        address=order.getDelivery().getAddress();
    }
    }

}
