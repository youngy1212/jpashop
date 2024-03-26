package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        //왜 여기서 조회하는거? 이미 controller단에서 조회해서넘어오면 안되나?
        //가능하지만, 여기서 조회까지 진행하는 편이 좋음.
        //영속성 상태로 진행할 수 있음.
        //조회는 상관없음 하지만, 핵심 비지니스 로직 CUD의 경우 안에서 하는 편이 좋음
        //영속성 상태에서 조회할 수 있기 때문에 (더티체킹)

        //엔티티 조회 (Id만 넘어오기 떄문)
        Member member = memberRepository.findById(memberId).get();
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item,item.getPrice(),count);

        //주문생성
        Order order = Order.createOrder(member,delivery,orderItem);

        //주문 저장
        orderRepository.save(order);
        //근데 오더만 save하면 배송정보 orderItem 등은?
        //order에 Casecade가 걸려있음 -> 자동으로 저장

        return order.getId(); //식별자 반환
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel(); //이미 비지니스 로직이 있기 때문
    }

    /**
     * 주문 검색
     */
     public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
     }



}
