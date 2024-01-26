package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //스프링빈으로 등록 (컴포넌트 대상) srpingBootApplication 하위를 전부 스킨(그 중 하나)
public class MemberRepository {

    @PersistenceContext //jpa 엔티티 매니저를 주입해줌.
    private EntityManager em;

    //만약 내가 직접 factory를 주입하고싶다. 쓸일은 거의 없음.
   /* @PersistenceUnit
    private EntityManagerFactory emf;*/

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){ //단건조회, id를 넣으면 멤버를 찾아 반환
        return em.find(Member.class, id); //find jpa가 제공하는 메서드
    } //첫번째 타입, 두번째 pk

    public List<Member> finsAll(){
        return   em.createQuery("select m from Member m", Member.class)
                .getResultList();
    } //쿼리의 대상이 enetity

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }

}
