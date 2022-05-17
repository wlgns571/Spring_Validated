package com.study.member.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.valid.Step1;
import com.study.common.valid.Step2;
import com.study.common.valid.Step3;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.member.service.IMemberService;
import com.study.member.vo.MemberVO;

@SessionAttributes("member")
// model에 담긴 member의 값을 유지시키는 용도, Controller내에서만 값이 유지
@Controller
public class MemberJoinController {
	
	@Inject
	ICommCodeService codeService;
	
	@Inject
	IMemberService memberService;
	
	// SessionAttributes 사용할때 필수로 작성
	@ModelAttribute("member")
	public MemberVO member() {
		return new MemberVO();
	}
	
	@ModelAttribute("jobList")
	public List<CodeVO> jobList() {
		return codeService.getCodeListByParent("JB00");
	}
	@ModelAttribute("hobbyList")
	public List<CodeVO> hobbyList() {
		return codeService.getCodeListByParent("HB00");
	}
	
	@RequestMapping("/join/step1.wow")
	public String step1(@ModelAttribute("member") MemberVO member) {
		
		return "join/step1";
	}
	@RequestMapping("/join/step2.wow")
	public String step2(@Validated(value = {Step1.class})@ModelAttribute("member") MemberVO member,
			BindingResult error) {
		if (error.hasErrors()) {
			return "join/step1";
		}
		return "join/step2";
	}
	@RequestMapping("/join/step3.wow")
	public String step3(@Validated(value = {Step2.class})@ModelAttribute("member") MemberVO member,
			BindingResult error) {
		if (error.hasErrors()) {
			return "join/step2";
		}
		return "join/step3";
	}
	@RequestMapping("/join/regist.wow")
	public String regist(Model model, @Validated(value = {Step3.class})@ModelAttribute("member") MemberVO member,
			BindingResult error, SessionStatus status) {
		if (error.hasErrors()) {
			return "join/step3";
		}
		System.out.println(member);
		status.setComplete(); // SessionAttribute에 저장되어있던 내용 클리어
		
		ResultMessageVO resultMessageVO = new ResultMessageVO();
		try {
			memberService.registMember(member);
			resultMessageVO.messageSetting(true, "회원 등록", "회원 등록 성공했습니다.", "/member/memberList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "회원 등록 실패", "회원 등록에 실패했습니다", "/member/memberList.wow", "목록으로");
		} catch (BizDuplicateKeyException ede) {
			resultMessageVO.messageSetting(false, "회원 등록 실패", "회원 아이디가 이미 존재합니다.", "/member/memberList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO",resultMessageVO);
		// member DB에 insert하면 됨.
		return "common/message";
	}
	@RequestMapping("/join/cancel.wow")
	public String cancel(SessionStatus status) {
		status.setComplete(); // 취소
		return "redirect:/";
	}
}
