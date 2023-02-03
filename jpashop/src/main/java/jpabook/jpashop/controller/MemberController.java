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
    public String create(@Valid MemberForm form, BindingResult result) {
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

}
