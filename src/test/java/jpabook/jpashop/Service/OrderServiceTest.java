package jpabook.jpashop.Service;

import jpabook.jpashop.Exception.NotEnoughStockException;
import jpabook.jpashop.Repository.OrderRepository;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book= new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        int OrderCount = 2;

        //when
        Long orderId=orderService.order(member.getId(), book.getId(), OrderCount);
        //then
        Order order=orderRepository.findOne(orderId);

        Assert.assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,order.getStatus());
        Assert.assertEquals("주문한 상품 종류수가 정확해야한다",1,order.getOrderItems().size());
        Assert.assertEquals("주문 가격은 가격*수량이다",10000*OrderCount,order.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야 한다",8,book.getStockQuantity());
    }
    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book= new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        int OrderCount = 2;
        Long OrderId= orderService.order(member.getId(), book.getId(), OrderCount);
        //when
        orderService.cancelOrder(OrderId);
        //then
        Order getOrder=orderRepository.findOne(OrderId);

        assertEquals("주문 취소시 상태는 Cancel이다.",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.",10,book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);

        Book book= new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount=11;
        //when
        orderService.order(member.getId(),book.getId(),orderCount);
        //then
        fail("재고 수량이 부족해야 한다.");
    }

}