package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//상속관계의 전략 지정 SINGLE_TABLE
@DiscriminatorColumn(name = "dtype")
//원테이블이니까 구분할수 있어야함. 해당
public abstract class Item { //추상체

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories =  new ArrayList<>();

}
