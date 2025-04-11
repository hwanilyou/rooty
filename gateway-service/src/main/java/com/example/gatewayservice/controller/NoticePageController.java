package com.example.gatewayservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticePageController {

    @GetMapping("/write")
    public String noticeWritePage() {
        return "notice/write"; // templates/notice/notice_write.html
    }

    @GetMapping("/notice")
    public String noticeListPage() {
        return "notice/notice_list"; // templates/notice/notice_list.html
    }

    @GetMapping("/view/{id}")
    public String noticeDetailPage() {
        return "notice/view"; // templates/notice/notice_view.html
    }
}
