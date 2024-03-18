package jpabook.jpashop.repository.order.simpleRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    //이쪽에 controller 의존관계 생기면 안됨. 한방향으로 흘러야함
    //OrderSimpleQueryDto dto 따로 생성
    public List<jpabook.jpashop.repository.OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.OrderSimpleQueryDto(o.id, m.name," +
                        " o.orderDate, o.status, d.address)" +
                        " from Order o"+
                        " join o.member m" +
                        " join o.delivery d", jpabook.jpashop.repository.OrderSimpleQueryDto.class
        ).getResultList();

        //OrderSimpleQueryDto dto는 반환할 수 없음 그래서 new Operation 필수
        //엔티티로 바로넘기면 식별자로 넘어감 (o)로 넘길수 없음(각각 넘기기)
    }
}
