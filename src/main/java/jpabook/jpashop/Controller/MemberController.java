package jpabook.jpashop.Controller;


import jpabook.jpashop.Service.MemberService;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.web.MemberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")  /** @valid는 여기서 Not Empty(MemberForm 체크)를 체크해주는 기능
                                      BindingResult는 에러를 바인딩해 체크하는 것이 가능해짐*/
    public String create(@Valid MemberForm memberForm, BindingResult result){
        if(result.hasErrors()){
            return "members/createMemberForm";
        }
        Address new_address= new Address(memberForm.getCity(), memberForm.getStreet(),memberForm.getZipcode());
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(new_address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> memberList= memberService.findAll();
        model.addAttribute("members",memberList);
        return "members/memberList";
    }
}
