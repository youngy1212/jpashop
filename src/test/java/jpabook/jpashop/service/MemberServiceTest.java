package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional //test케이스에서는 자동롤빽
public class MemberServiceTest {
    //테스트 케이스
    /*1. 회원가입을 성공해야 한다.
      2. 회원가입 할 때 같은 이름이 있으면 예외가 발생해야 한다.*/

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    //나는 그래도 쿼리가 보고싶어!
    @Autowired EntityManager em;

    @Test
    //@Rollback(false) //inesrt문 확인을 위해 false시켜놓음 방법1.
    public void 회원가입() throws Exception{

        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();//db에 반영(test지만) 실제 트랜잭션은 Rollback을 하게됨 방법2.
        assertEquals(member, memberRepository.findOne(saveId));
        //값이 같은지 체크
    }
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{

        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외가 발생해야함! 이글이 보이면 안돼용~~");

    }

}