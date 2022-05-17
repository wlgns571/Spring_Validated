package com.study.free.web;

import java.util.List;

import javax.inject.Inject;
import javax.validation.groups.Default;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.valid.Modify;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.service.IFreeBoardService;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

@Controller
public class FreeBoardController {
	@Inject
	IFreeBoardService freeBoardService;
	@Inject 
	ICommCodeService codeService;
	
	// edit이랑 from, list에는 cateList가 공통으로 model에 담긴다....공통처리
	@ModelAttribute("cateList")
	public List<CodeVO> catelist() {
		return codeService.getCodeListByParent("BC00");
	}
	
	@RequestMapping("/free/freeList.wow")
	public String freeBoardList(Model model, @ModelAttribute("searchVO")FreeBoardSearchVO searchVO) {
		List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(searchVO);
		model.addAttribute("freeBoardList", freeBoardList);
		return "free/freeList";
	}
	
	@RequestMapping(value="/free/freeView.wow")
	public String freeBoardView(Model model,@RequestParam(required = true, name = "boNo" ) int boNo) {
		try{
			FreeBoardVO freeBoard=freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
			freeBoardService.increaseHit(boNo);
		}catch (BizNotFoundException enf){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}catch (BizNotEffectedException ene){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글NotEffected", "업데이트에 실패했습니다",
					"/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "free/freeView";
	}
	
//	@RequestMapping("/free/freeEdit.wow")
//	public ModelAndView freeBoardEdit(int boNo) {
//		ModelAndView mav = new ModelAndView();
//		try{
//			FreeBoardVO freeBoard=freeBoardService.getBoard(boNo);
//			mav.addObject("freeBoard", freeBoard);
//		}catch (BizNotFoundException enf){
//			ResultMessageVO resultMessageVO= new ResultMessageVO();
//			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
//					"/free/freeList.wow", "목록으로");
//			mav.addObject("resultMessageVO", resultMessageVO);
//			mav.setViewName("common/message");
//		}
//		List<CodeVO> cateList=codeService.getCodeListByParent("BC00");
//		mav.addObject("cateList", cateList);
//		mav.setViewName("free/freeEdit");
//		
//		return mav;
//	}
	@RequestMapping("/free/freeEdit.wow")
	public String freeBoardEdit(Model model, int boNo) {
		try{
			FreeBoardVO freeBoard=freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
		}catch (BizNotFoundException enf){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "free/freeEdit";
	}
	
	@RequestMapping(value = "/free/freeModify.wow", method = RequestMethod.POST)
	public String freeBoardModify(Model model
			,@Validated(value = {Modify.class, Default.class}) @ModelAttribute("freeBoard")FreeBoardVO freeBoard
			, BindingResult error) {
		if (error.hasErrors()) {
			return "free/freeEdit";
		}
		ResultMessageVO resultMessageVO=new ResultMessageVO();
		try {
			freeBoardService.modifyBoard(freeBoard);
			resultMessageVO.messageSetting(true, "수정", "수정성공"
				,"/free/freeList.wow" , "목록으로");
		} catch (BizNotFoundException enf) {
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizPasswordNotMatchedException epm) {
			resultMessageVO.messageSetting(false, "비밀번호틀림", "글쓸때의 비밀번호랑 다릅니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "글NotEffected", "업데이트에 실패했습니다",
					"/free/freeList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}
	
	@PostMapping("/free/freeRegist.wow")
	public String freeBoardRegist(Model model
			,@Validated() @ModelAttribute("freeBoard") FreeBoardVO freeBoard
			,BindingResult error) {
		// FreeBoardVO는 사용자가 넘긴 데이터인데 이를 검사하고 싶다.
		// CASE1: 검사해서 문제가 있는 경우 => 다시 freeForm.jsp로 가야한다.
			if (error.hasErrors()) {
				return "free/freeForm";
			}
		// CASE2: 검사해서 문제가 없는 경우 => 기존 코드 그대로 실행
		ResultMessageVO resultMessageVO=new ResultMessageVO();
		try {
			freeBoardService.registBoard(freeBoard);
			resultMessageVO.messageSetting(true, "등록", "등록성공", "free/freeList.wow"
					 ,"목록으로");
		} catch (BizNotEffectedException ebe) {
			resultMessageVO.messageSetting(false, "실패", "업데이트실패", "free/freeList.wow"
					 ,"목록으로");
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}
	
//	private boolean isHaveProblemFreeBoardRegist(FreeBoardVO freeBoard) {
//		if (freeBoard.getBoWriter().isEmpty()) return true;
//		if (freeBoard.getBoCategory().isEmpty()) return true;
//		if (freeBoard.getBoPass().isEmpty() || freeBoard.getBoPass().length() < 4) return true;
//		// 나머지 정보도 하나하나 검사...
//		return false;
//	}
	
	@RequestMapping("/free/freeForm.wow")
	public String freeBoardForm(Model model, @ModelAttribute("freeBoard") FreeBoardVO freeBoard) {
		return "free/freeForm";
	}

	@PostMapping("/free/freeDelete.wow")
	public String freeBoardDelete(Model model, @ModelAttribute("freeBoard")FreeBoardVO freeBoard) {
		ResultMessageVO resultMessageVO=new ResultMessageVO();
		try {
			freeBoardService.removeBoard(freeBoard);
			resultMessageVO.messageSetting(true, "삭제", "삭제성공"
					,"/free/freeList.wow" , "목록으로");
		}catch (BizNotFoundException enf) {
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizPasswordNotMatchedException epm) {
			resultMessageVO.messageSetting(false, "비밀번호틀림", "글쓸때의 비밀번호랑 다릅니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "글NotEffected", "업데이트에 실패했습니다",
					"/free/freeList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}
}
