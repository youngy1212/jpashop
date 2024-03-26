package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //JpaRepository에 findAll , save, findByall등 대부분 구현되어있음
    //구현체도 알아서

    //select m from Member m where m.name =? 이라는 JPA 쿼리를 마음대로 만들어줌
    List<Member> findByName(String name);



}
