package jpabook.jpashop.Service;

import jpabook.jpashop.Repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final이 있는 멤버변수에만 넣어서 생성자를 만들어줌.
public class MemberService {
    /**
     * @Autowired 는 등록됨으로 변경을 할 수가 없게 된다. 따라서 setter injection을 활용 --> 만약에 실행중에 바꾼다면? 바꿀 일 없음 하지만 새로 넣는게 가능한
     * 영역이 되어버린다. 그렇다면?
     */

    private final MemberRepository memberRepository;


    /**
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository){
        this.memberRepository=memberRepository;
    }
    */

    //회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findmMembers=memberRepository.findByName(member.getName());
        if(!findmMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    //회원 전체조회

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.find(memberId);
    }
}
