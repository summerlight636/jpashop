package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
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

    @GetMapping("/members/new") //get은 열어보는 역할
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm()); //Controller에서 View로 넘어갈 때 여기에 데이터를 심어서 넘긴다.
        //화면 넘어갈 때 memberForm이라는 빈 껍데기 멤버 객체를 넣어 전달
        return "members/createMemberForm"; //넘어갈 화면 이름
    }

    @PostMapping("/members/new") //post는 이 데이터를 실제 등록하는 역할
    public String create(@Valid MemberForm form, BindingResult result) { //Member Entity 를 그대로 쓰기보다는 MemberForm 을 따로 생성하여 사용 (1:1로 매칭되기가 쉽지 않기 때문)
        //회원 이름을 필수로 쓰고 싶다: @Valid 어노테이션을 사용하면 스프링이 javax validation 기능을 쓰는구나라고 인식. 이 폼의 @NotEmpty 조건을 인식
        //validate한 거 다음에 VindingResult가 있으면 오류가 여기 담겨서 코드가 실행이 된다.
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; //저장이 되고 나면 다시 재 로딩되거나 하면 안 좋기 때문에 리다이렉트로 첫번째 페이지로 보내버린다.
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers(); //API를 만들 때에는 이유를 불문하고 엔티티를 넘기면 안 된다.
        //password가 그대로 노출되는 문제
        //API 스펙이 변해버린다. 엔티티에 로직을 추가했는데 그것 때문에 API 스펙이 변해버린다. (?) 결과적으로 불안정한 API 스펙이 되어버린다.
        model.addAttribute("members", members); //이것도 form 객체를 따로 만들거나 DTO를 만드는 것이 더 현명한 것 ㅇ
        return "members/memberList";
    }
}
