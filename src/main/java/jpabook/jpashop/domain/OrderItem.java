package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//protected 기본생성자 (다른곳에서 set하며 사용하지 말라고 막는것 )
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY) //static import
    @JoinColumn(name = "item_id")
    private Item item; //주문 상품

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량

    //==생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){ // 얼마에 item을 삿어

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //넘어온만큼 재고 -
        return orderItem;
    }

    //== 비지니스 로직 ==//
    public void cancel(){
       getItem().addStock(count); //재구수량 원복
    }

    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
