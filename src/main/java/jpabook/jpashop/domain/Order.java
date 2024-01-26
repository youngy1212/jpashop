package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name="orders") //order는 여러군데 사용되기때문에 이름 변경
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //member와 다(나)대1(member)의 관계 해당 PK의 이름이 member_id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //양뱡향 매핑의 경우 연관관계의 주인을 정해줘야함!
    //order에서 값이 바뀌면 member 값이 바뀔수 있고, 반대의 경우가 있을 수 있음
    //둘다 바꿔버리면 jpa는 뭘보고 확인할지 알 수 없음
    //PK를 업데이트 하는 경우 둘 중 하나만 할 수 있도록 맞춰야아함.(주인)
    //** PK와 가까운 테이블을 연관관계의 주인으로 잡아야 함.)


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    //CascadeType order를 저장하면 orderItems 같이 저장된다.
    //ALl로 해놓았기 떄문에 delete 할떄도 함께 지워짐


    //오더를 저장할때 delivery도 같이 persist 해줌.
    //원래면 오더 persist , 딜리버리 persist 해줘야하지만, 이어서 캐스캐이드 해줌,
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
    //OneToOne의 관계에는 pk를 어디에 둬야 할까?
    //주로 자주사용하는 곳이 둔다. 어느쪽에 두어도 상관은 없음.
    //자주 사용하는 Order의 Delibery를 Pk로 잡겠다.


    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING) //Enumer의 경우
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //물론 여기가 연관관계의 주인이기때문에 member와 order
    //order만 업데이트하면 되지만 양방향이기 떄문에 연관관계 메서드를 만들어주자
    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    //order와 member는 양방향이기 때문에 데이터를 서고 주고받아야하니, 묶는 메서드를 만듬

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //연관관계 메서드의 경우 컨트롤 하는쪽이 들고있는게 좋음.

}
