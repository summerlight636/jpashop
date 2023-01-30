package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //JPA의 모든 데이터 변경이나 로직들은 가급적 트랜잭션 안에서 실행되어야 함
@RequiredArgsConstructor //final이 있는 필드만 가지고 생성자를 만들어 줌
public class MemberService {

    //생성자 인젝션 //@RequiredArgsConstructor 덕에 한 줄만으로 인젝션이 가능해짐
    private final MemberRepository memberRepository; //변경할 일이 없기 때문에 final //final을 붙이면 컴파일 시점에 체크할 수 있는 장점

//    //@Autowired //생성자가 하나만 있는 경우에는 어노테이션 없이도 스프링이 자동으로 인젝션 해준다.
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //필드 인젝션 단점: test하거나 할 때 바꿀 수 없음
//    @Autowired
//    private MemberRepository memberRepository;

    //세터 인젝션: test코드 작성시 직접 주입 가능
//    private MemberRepository memberRepository;
//
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional //쓰기 까지 필요하기 때문에 readOnly x(디폴트 값). 옵션이 다르기 때문에 한 번 더 명시
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    //실제 상황에서는 이것만으로 충분하지 않음.
    //DB 상에서 name을 unique 제약 조건으로 잡아주는 것을 권장
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}

