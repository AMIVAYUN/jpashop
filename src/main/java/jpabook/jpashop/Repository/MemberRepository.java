package jpabook.jpashop.Repository;


import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    @PersistenceContext  // @AutoWired는 안되고 @PersistenceContext 어노테이션으로 인젝션 해주어야함
    private final EntityManager em;


    public Long save(Member member){
        em.persist(member);
        return member.getId();

    }
    public Member find(Long id){
        return em.find(Member.class,id);
    }
    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class).getResultList();
    }
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name=:name",Member.class).setParameter("name",name).getResultList();
    }
}
