package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;
//패키지도 분리하는게 좋음
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderQuerySerice {

    private final OrderRepository orderRepository;

    //OSIV를 끌경우 지연로딩이 안됌 (프록시 초기화를 해야하는데 없어)
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }

}
