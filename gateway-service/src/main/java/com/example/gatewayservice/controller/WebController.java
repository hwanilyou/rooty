package com.example.gatewayservice.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("message", "회원 가입");
        return "register";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("message", "상품 목록");
        return "products";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("message", "주문 목록");
        return "orders";
    }

    @GetMapping("/order-complete")
    public String orderComplete(Model model) {
        model.addAttribute("message", "주문 완료 현황");
        return "order-complete";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("message", "관리자 페이지");
        return "admin";
    }

    // flight 관련 뷰 매핑
    @GetMapping("/flight/admin-list")
    public String flightAdminList(Model model) {
        model.addAttribute("message", "항공편 목록");
        return "flight/admin-flight-list";
    }

    @GetMapping("/flight/admin-register")
    public String flightAdminRegister(Model model) {
        model.addAttribute("message", "신규 항공편 등록");
        return "flight/admin-flight-register";
    }

    //    @GetMapping("/flight/admin-edit/{id}")
//    public String flightAdminEdit(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("message", "항공편 수정: " + id);
//        return "flight/admin-flight-edit";
//    }
    @GetMapping("/flight/admin-edit/{id}")
    public String flightAdminEdit() {
        // 모델 없이 뷰 이름만 반환
        return "flight/admin-flight-edit";
    }

    // 예약 관련 매핑 (reservation 폴더 내 view 파일 사용)
    @GetMapping("/reservation/admin-reservation-list")
    public String reservationList(Model model) {
        model.addAttribute("message", "예약 목록을 확인합니다.");
        return "reservation/admin-reservation-list"; // templates/reservation/admin-reservation-list.html
    }

    @GetMapping("/reservation/admin-reservation-edit")
    public String reservationEdit(@RequestParam("id") Long reservationId, Model model) {
        model.addAttribute("reservationId", reservationId);
        model.addAttribute("message", "예약 상세 및 수정 페이지");
        return "reservation/admin-reservation-edit"; // templates/reservation/admin-reservation-edit.html
    }

    @GetMapping("/deleteReservation")
    public String deleteReservation(@RequestParam("id") Long reservationId, Model model) {
        model.addAttribute("message", "예약 ID " + reservationId + " 삭제 완료");
        return "redirect:/reservation/admin-reservation-list"; // redirect to reservation list
    }

    @GetMapping("/list")
    public String list() {
        return "list";
    }

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @GetMapping("/view/{id}")
    public String view() {
        return "view";
    }

    @GetMapping("/edit/{id}")
    public String edit() {
        return "edit";
    }
}