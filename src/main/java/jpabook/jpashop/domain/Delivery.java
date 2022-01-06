package jpabook.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // 이거 ordinal 하면 정수형으로 0,1 저장되서 중간에 모드 추가삽입하면 뒤에 데베 다 날라감 조심.
    private Deliverystatus status;//READY, COMP


}
