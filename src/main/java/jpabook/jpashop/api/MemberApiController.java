package jpabook.jpashop.api;


import jakarta.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController //Controller와 RequestBody 를 합친 어노테인션
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }  //이렇게 엔티티를 직접 반환하면 안됌!

    @GetMapping("/api/v2/members")
    public Result memberV2(){
        List<Member> findMember = memberService.findMembers();
        List<MemberDto> collect = findMember.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data @AllArgsConstructor
    static class Result<T> {
        private T date;
    }

    @Data @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){ //json 데이터를 member로 바꿔줌
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    } //엔티티를 API를 노출하면 안됌!

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.name);

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid  UpdateMemberRequest request ){
        //등록과 수정은 많이 달라 별도의 response와 request를 만들어줌

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id); //서비스에서 return하지 않고, 여기서 조회하는 이유 (커맨드와 쿼리를 분리하기 위해)
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }

    //updateMemberV2 는 회원정보를 부분 업데이트 한다.
    //여기는 PUT 방식을 사용했지만, PUT은 전체 업데이트를 할 때 사용하는 것이 맞다.
    //부분 업데이트를 하려면 PATCH를 사용하거나 POST를 사용하는 것이 PEST 스타일에 맞다.


    //밖에서 사용하지 않고. controller 내부에서만 사용해서 안에서 dto를 만듬
    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor //dto는 변경하고 그러지않아, 롬복 어노테이션을 씀
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }


    @Data
    static class CreateMemberRequest{
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }



}
