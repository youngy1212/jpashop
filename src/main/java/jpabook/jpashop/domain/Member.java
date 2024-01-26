package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String name;

    @Embedded //내장타입이라는 뜻
    private Address address;

    @OneToMany(mappedBy= "member")
    //나는 연관관계의 주인이 아니고, order의 member에 의해 연동된거야!(읽기전용)
    private List<Order> orders = new ArrayList<>();

    //컬렉션은 필드에서 초기화 하자.


}
