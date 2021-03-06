package jpabook.jpashop.api;


import jpabook.jpashop.Repository.OrderRepository;
import jpabook.jpashop.Repository.OrderSearch;
import jpabook.jpashop.Repository.query.OrderQueryDto;
import jpabook.jpashop.Repository.query.OrderQueryRepository;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.hibernate.loader.custom.Return;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all= orderRepository.findAllByString(new OrderSearch());
        for(Order order:all){
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems=order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders=orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collct=orders.stream().map(order -> new OrderDto(order)).collect(Collectors.toList());
        return collct;

    }
    @GetMapping("api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order>order=orderRepository.findAllWithItem();
        List<OrderDto> orderDtos=order.stream().map(OrderDto::new).collect(Collectors.toList());
        return orderDtos;
    }
    @GetMapping("api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(value = "offset",defaultValue = "0")int offset,
                                       @RequestParam(value = "limit",defaultValue = "100")int limit){
        List<Order>order=orderRepository.findWithMemberDelivery(offset,limit);
        List<OrderDto> orderDtos=order.stream().map(OrderDto::new).collect(Collectors.toList());
        return orderDtos;
    }
    @GetMapping("api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }
    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            this.orderId=order.getId();
            this.name=order.getMember().getName();
            this.orderDate=order.getOrderDate();
            this.orderStatus=order.getStatus();
            this.address=order.getDelivery().getAddress();
            order.getOrderItems().stream().forEach(orderItem -> orderItem.getItem().getName());
            this.orderItems=order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());

        }
    }

    @Getter
    static class OrderItemDto{
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            itemName=orderItem.getItem().getName();
            orderPrice=orderItem.getOrderPrice();
            count=orderItem.getCount();

        }
    }


}
