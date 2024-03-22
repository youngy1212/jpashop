package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

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


    //==비즈니스 로즉 ==//
    //재고를 늘리고 줄이기 : 왜 여기?  stockQuantity 데이터가 있는곳에 비니지스 로직이 있는 편이 좋음
    /***
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
        /***
         * 데이터를 계산하고 set 넣는 방식으로 많이 사용했을 것
         * 하지만 객체지향적으로 보았을떄 비지니스로직은
         * 비지니스 메소드에 있는 것이 가장 좋음
         * stockQuantity 의 정보를 여기에 가지고 있기 때문에 
         * 핵심 비지니스 로직을 entity에 직접 넣음
         */
    }

    /***
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
