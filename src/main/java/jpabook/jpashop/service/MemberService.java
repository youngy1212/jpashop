package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//데이터 변경은 트랜잭션 안에있어야함.
@Transactional(readOnly = true) // 읽기전용(선능최적화)
@RequiredArgsConstructor //알아서 생성자를 만들어줌
public class MemberService {

    @Autowired
    private final MemberRepository memberRepository;
    //필드 ** 주입하기 까다로움

  /*  public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository; //생성자 인젝션을 사용
    }*/
    //스프링이 뜰때 생성자에서 인젝션을 해줌. Autowired가 없어도 생성자 하나만 있다면


    /*
        회원가입
     */
    @Transactional //변경
    public Long join(Member member){

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member); //문제없으면 save
        return member.getId();
    }

    //멀티스레드등의 조건이 있기 떄문에 멤버 네임을 유니크 제약으로 잡기

    private void validateDuplicateMember(Member member){

        //EXCEPTION 문제확인, 같은 이름이 있는지 확인
        List<Member> findMebers = memberRepository.findByName(member.getName());
        if(!findMebers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */

    public List<Member> findMembers() {
        return memberRepository.finsAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

    //보통은 읽기가 많으니, readOnly = true 해주고 일부를 변경

}
