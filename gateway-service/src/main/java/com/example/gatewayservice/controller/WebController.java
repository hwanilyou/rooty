package com.example.gatewayservice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "MSA 프로젝트에 오신 것을 환영합니다");
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("message", "로그인");
        return "login/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "회원 가입");
        return "login/register";
    }


    // flight 관련 매핑 추가
    @GetMapping("/flight/admin-list")
    public String flightAdminList(Model model) {
        // 예시: 항공편 목록 페이지에 필요한 데이터를 model에 추가
        model.addAttribute("message", "항공편 목록");
        return "flight/admin-flight-list"; // templates/flight/admin-flight-list.html
    }

    @GetMapping("/flight/admin-register")
    public String flightAdminRegister(Model model) {
        model.addAttribute("message", "신규 항공편 등록");
        return "flight/admin-flight-register"; // templates/flight/admin-flight-register.html
    }

    @GetMapping("/flight/admin-edit/{id}")
    public String flightAdminEdit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("message", "항공편 수정: " + id);
        // 실제 항공편 데이터를 model에 추가하면 됨
        return "flight/admin-flight-edit"; // templates/flight/admin-flight-edit.html
    }

    // 예약관리 관련 매핑 (Thymeleaf 템플릿 사용)
    @GetMapping("/reservation/admin-reservation-list")
    public String reservationList(Model model) {
        model.addAttribute("message", "예약 목록을 확인합니다.");
        return "reservation/admin-reservation-list";
    }

    @GetMapping("/reservation/admin-reservation-edit")
    public String reservationEdit(@RequestParam("id") Long reservationId, Model model) {
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("message", "예약 상세 및 수정 페이지");
        return "reservation/admin-reservation-edit"; // templates/reservation/admin-reservation-edit.html

    }

    //일반 유저 내 예약 관리
    @GetMapping("/reservation/user-reservation-list")
    public String userReservationList(Model model) {
        model.addAttribute("message", "내 예약 관리");
        return "reservation/user-reservation-list";
    }

    //수정아님 상세보기로 바꿈
    @GetMapping("/reservation/user-reservation-edit")
    public String userReservationEdit(@RequestParam("id") Long reservationId, Model model) {
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("message", "내 예약 상세 및 수정 페이지");
        return "reservation/user-reservation-edit";
    }


//    @GetMapping("/reservation/seat-edit")
//    public String seatEditPage() {
//        return "reservation/seat-edit"; // templates/reservation/seat-edit.html 을 렌더링
//    }



    @GetMapping("/deleteReservation")
    public String deleteReservation(@RequestParam("id") Long reservationId, Model model) {
        model.addAttribute("message", "예약 ID " + reservationId + " 삭제 완료");
        return "redirect:/reservation/admin-reservation-list"; // redirect to reservation list
    }

    //role 세션 저장용
    @PostMapping("/setSession")
    public String setSessionFromFrontend(@RequestParam String username,
                                         @RequestParam String role,
                                         HttpSession session) {
        session.setAttribute("username", username);
        session.setAttribute("role", role);
        return "redirect:/"; // 또는 관리자라면 /admin-flight-list로 이동
    }

    //관리자 전용 페이지 접근 제어
    @GetMapping("/flight/admin-flight-list")
    public String adminFlightList() {
        return "flight/admin-flight-list";
    }



    @GetMapping("/mypage")
    public String serveMyPage() {
        return "mypage/mypage"; // → templates/mypage.html을 렌더링 (Thymeleaf 사용 시)
    }

    @GetMapping("/members")
    public String showMemberListPage() {
        return "mypage/members"; // → templates/admin/members.html 로 연결됨
    }



    @GetMapping("/booking")
    public String booking(){
        return "booking/booking";
    }

    @GetMapping("/seat-selection")
    public String seatSelection(Model model){
        return "booking/seat-selection";
    }


} 