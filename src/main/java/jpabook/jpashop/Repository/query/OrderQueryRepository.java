package jpabook.jpashop.Repository.query;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderQueryDto> findOrders() {

        return em.createQuery("select new jpabook.jpashop.Repository.query.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address) from Order o "
                +"join o.member m"+" join o.delivery d",OrderQueryDto.class).getResultList();

    }

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto>result=findOrders();
        result.forEach(orderQueryDto -> {
            List<OrderItemQueryDto> orderItems=findOrderItems(orderQueryDto.getOrderId());
            orderQueryDto.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.Repository.query.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count) from OrderItem oi"
                +" join oi.item i" +" where oi.order.id=:orderId",OrderItemQueryDto.class).setParameter("orderId",orderId).getResultList();
    }
}
