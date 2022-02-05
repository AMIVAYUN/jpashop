package jpabook.jpashop.api;


import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController//@Controller+@ResponseBody
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMember(@RequestBody @Valid Member member){
        Long id=memberService.join(member);
        return new CreateMemberResponse(id);

    }
    @Data
    static class CreateMemberResponse{
        CreateMemberResponse(Long id){
            this.id=id;
        }
        private Long id;


    }
    @Data
    static class CreateMemberResponse2{

        private String name;


    }
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMember2(@RequestBody @Valid CreateMemberResponse2 request){
        Member member=new Member();
        member.setName(request.name);
        Long id=memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id")Long id,@RequestBody @Valid UpdateMemberRequest request){
        memberService.update(id,request.getName());
        Member member=memberService.findOne(id);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }
    @Data
    static class UpdateMemberRequest{
        String name;
    }
    @GetMapping("api/v1/members")
    public List<Member> membersv1(){
        return memberService.findAll();
    }
    @GetMapping("api/v2/members")
    public Result membersv2(){
        List<Member> findMembers=memberService.findAll();
        List<MemberDTO> collect=findMembers.stream()
                .map(m->new MemberDTO(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;

    }
    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String name;
    }
}
