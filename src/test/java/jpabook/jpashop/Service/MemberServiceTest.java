package jpabook.jpashop.Service;

import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)//메모리 모드 +DB 엮어서 스프링 부트 올려서 실험하는법
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("윤주석");
        //when
        Long saveId=memberService.join(member);
        //then
        assertEquals(member,memberRepository.find(saveId));
    }
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1= new Member();
        member1.setName("Kim");
        Member member2= new Member();
        member2.setName("Kim");
        //when
        Long saveId=memberService.join(member1);
        Long saveId2=memberService.join(member2);


        //then
        fail("예외가 발생해야함");
    }
}