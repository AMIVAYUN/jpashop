package jpabook.jpashop.Repository;


import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long saveid){
        return em.find(Order.class, saveid);
    }
    public List<Order> findAllByString(OrderSearch orderSearch) {
//language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
    public List<Order> findAll(OrderSearch orderSearch){
        /**
         * 동적 쿼리를 문자열로 만들었을 때 --> 복잡함 버그 발생 가능성 매우 높다.
         */
        String jpql="select o from Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if(orderSearch.getOrderStatus()!=null){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            }else{
                jpql +=" and";
            }
            jpql +=" o.status = :status";
        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql+=" where";
                isFirstCondition = false;

            }else{
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query=em.createQuery(jpql, Order.class).setMaxResults(1000);

        if(orderSearch.getOrderStatus() !=null){
            query=query.setParameter("status", orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())){
            query=query.setParameter("name",orderSearch.getMemberName());
        }
        return query.getResultList();

        /**
         *

        return em.createQuery("select o from Order o join o.member m"+
                "where o.status= :status" +
                "and m.name like :name",Order.class)
                .setParameter("status",orderSearch.getOrderstatus())
                .setParameter("name", orderSearch.getMemberName())
                .setFirstResult(0)
                .setMaxResults(1000) //최대 1000건
                .getResultList();
         */
    }

    /**
     * JPA Criteria
     * 치명적 단점: 유지 보수가 0에 가까워짐 --> 이 말이 무슨 뜻이냐 아래 코드를 보고 무슨 쿼리인지 판단 가능? 그러니 그냥 할 수 있다! 라고만 REF
     * @param orderSearch
     * @return
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object,Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            Predicate status=cb.equal(o.get("status"),orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name=
                    cb.like(m.<String>get("name"),"%" + orderSearch.getMemberName() + "%");
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query= em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();


    }
    /**
    public List<Order>findByQueryDSL(OrderSearch orderSearch){
        QOrder order = QOrder.order;
    }
     */

}
