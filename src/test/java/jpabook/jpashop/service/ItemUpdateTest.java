package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{

        Book book = em.find(Book.class, 1);

        //TX
        book.setName("asdfe");

        //번경감지 == Dirty Checking
        //따로 update 하지 않아도 변경점을 찾아서 반영해줌. (영속성 컨텍스트 대상)

        //만약 한번 JPA에 들어갔다 나온 데이터라면? 준영속성 데이터
        //ex) book 수정의 경우 이미 기존 식별자를 가지고 있어서, 준영속성 이라고 할 수 있다.
        //JPA가 관리하지 않음. 변경관리가 일어나지 않음.

        //1. 변경감지 사용 itemService.updateItem 참고
        // 2. 병합(merge) 사용 ItemController.updateItem 참고
        // 아이템 레포지터리 보면 merge 호출하여 사용

        //변경감지를 사용할 경우 원하는 속성만 선택해서 변경,
        //병합을 사용할 경우 모든 속성이 변경 null 업데이트 위험
        //변경감지를 사용하는 것을 추천!



    }

}
