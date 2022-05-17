
<%@page import="com.study.code.vo.CodeVO"%>
<%@page import="com.study.code.service.CommCodeServiceImpl"%>
<%@page import="com.study.code.service.ICommCodeService"%>
<%@page import="com.study.exception.BizNotFoundException"%>
<%@page import="com.study.member.service.MemberServiceImpl"%>
<%@page import="com.study.member.service.IMemberService"%>
<%@page import="com.study.member.vo.MemberVO"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/inc/header.jsp"%>
</head>
<body>
	<%@include file="/WEB-INF/inc/top.jsp"%>




	<div class="container">
		<h3>회원 정보 수정</h3>
			<form:form action="memberModify.wow" method="post" modelAttribute="member">
				<table class="table table-striped table-bordered">
					<tbody>
						<tr>
							<th>아이디</th>
							<td>${member.memId } <input type="hidden" name="memId"
								value="${member.memId }"></td>
							<form:errors path="memId"></form:errors>
						</tr>
						<tr>
							<th>비밀번호</th>
							<td>
								<form:password path="memPass" cssClass="form-control input-sm"/>
								<form:errors path="memPass"></form:errors>	
							</td>
						</tr>
						<tr>
							<th>회원명</th>
							<td>
								<form:input path="memName" cssClass="form-control input-sm"/>
								<form:errors path="memName"></form:errors>	
							</td>
						</tr>
						<tr>
							<th>우편번호</th>
							<td>
								<form:input path="memZip" cssClass="form-control input-sm"/>
								<form:errors path="memZip"></form:errors>
							</td>
						</tr>
						<tr>
							<th>주소</th>
							<td>
								<form:input path="memAdd1" cssClass="form-control input-sm"/>
								<form:errors path="memAdd1"></form:errors>
								
								<form:input path="memAdd2" cssClass="form-control input-sm"/>
								<form:errors path="memAdd2"></form:errors>
							</td>
						</tr>
						<tr>
							<th>생일</th>
							<td>
							<input type="date" name="memBir"
								class="form-control input-sm" value='${member.memBir }'>
								<form:errors path="memBir"></form:errors>	
							</td>
						</tr>
						<tr>
							<th>메일</th>
							<td><input type="email" name="memMail"
								class="form-control input-sm" 
								value='${member.memMail }'>
								<form:errors path="memMail"></form:errors>
							</td>
						</tr>
						<tr>
							<th>핸드폰</th>
							<td><input type="tel" name="memHp"
								class="form-control input-sm" value='${member.memHp }'>
								<form:errors path="memHp"></form:errors>
							</td>
						</tr>
						<tr>
							<th>직업</th>
							<td>
								<form:select path="memJob" cssClass="form-control input-sm">
									<form:option value="">-- 직업 선택 --</form:option>
									<form:options items="${jobList }" itemLabel="commNm" itemValue="commCd"/>
								</form:select>
								<form:errors path="memJob"></form:errors>
							</td>
						</tr>
						<tr>
							<th>취미</th>
							<td>
								<form:select path="memHobby" cssClass="form-control input-sm">
									<form:option value="">-- 취미 선택 --</form:option>
									<form:options items="${hobbyList }" itemLabel="commNm" itemValue="commCd"/>
								</form:select>
								<form:errors path="memHobby"></form:errors>
							</td>
						</tr>
						<tr>
							<th>마일리지</th>
							<td>${member.memMileage }</td>
						</tr>
						<tr>
							<th>탈퇴여부</th>
							<td>${member.memDelYn }</td>
						</tr>
						<tr>
							<td colspan="2"><a href="memberList.wow"
								class="btn btn-info btn-sm"> <span
									class="glyphicon glyphicon-list" aria-hidden="true"></span>
									&nbsp;목록으로
							</a>
								<button type="submit" class="btn btn-primary">
									<span class="glyphicon glyphicon-heart" aria-hidden="true"></span>
									&nbsp;&nbsp; 저장
								</button>

								<button type="submit" formaction="memberDelete.wow"
									class="btn btn-danger btn-sm">
									<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
									&nbsp;&nbsp; 삭제
								</button></td>
						</tr>
					</tbody>
				</table>
			</form:form>
	</div>


</body>
</html>