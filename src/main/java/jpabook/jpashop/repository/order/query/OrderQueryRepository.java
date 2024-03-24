package jpabook.jpashop.repository.order.query;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
     //Repository 나눈이유, 진짜 orderRepository 는 엔티티 조회하는 등의 용도 (핵심비지니스)
    //이 Repository는 화면이나 api를 사용하기 위함, 화면에 fit 함
    //서로의 라이프사이클이 많이 다름
    private final EntityManager em;

    //켤렉션은 별도로 조회
    //Query : 루트1번, 컬렉션 N번
    //단건 조회에서 많이 사용하는 방식
    public List<OrderQueryDto> findOrderQueryDtos() {
        //루트 조회 (toOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders();

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행)
        result.forEach(o -> {
            //못채운 컬렉션을 넣어줌
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    // 1:N 관계인 orderItems 조회
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select  " +
                        "new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name," +
                        " oi.orderPrice, oi.count)" +
                        " from OrderItem oi"+
                        " join oi.item i "+
                        " where oi.order.id = :orderId",OrderItemQueryDto.class)
                .setParameter("orderId",orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders(){
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate," +
                                " o.status, d.address)" +
                                " from Order o"+
                                " join o.member m "+
                                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        //주문아이디 모음
        List<Long> orderIds = toOrderIds(result);
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        //쿼리를 한번 날리고 메모리에서 애를 앱으로 다 가져온 다음에 , 매칭해서 값을 세팅

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds"
                        , OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        //in으로 해서 넣어줌

        //orderItems 리스트로 한번에 뽑힘
        //성능 최적화를 위해 map을 바꿔서 넘겨줌 (groupingBy orderId)를 기준으로 그룹
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private static List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return orderIds;
    }


    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new "+
                    "jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate," +
                    " o.status, d.address, i.name, oi.orderPrice, oi.count )"+
                    " from Order o" +
                    " join o.member m" +
                    " join o.delivery d" +
                    " join o.orderItems oi" +
                    " join oi.item i", OrderFlatDto.class)
                .getResultList();

    }
}
