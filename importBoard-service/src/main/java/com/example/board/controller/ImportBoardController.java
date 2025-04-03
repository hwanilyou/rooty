package com.example.board.controller;


import com.example.board.entity.ImportBoard;
import com.example.board.entity.UserEntity;
import com.example.board.service.ImportBoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/import_board")
public class ImportBoardController {

    private final ImportBoardService importBoardService;

    // 공지사항 목록 페이지
    @GetMapping("/list")
    public String noticeList(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             HttpSession session) {
        Page<ImportBoard> pageResult = importBoardService.getImportBoardPage(page);
        List<ImportBoard> importBoards = pageResult.getContent();
        model.addAttribute("importBoards", importBoards);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageResult.getTotalPages());

        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        return "import_board/list";  // 목록 템플릿 (예: new.html)
    }

    // 게시글 상세보기 페이지 (GET /import_board/view/{no})
    @GetMapping("/view/{no}")
    public String viewNotice(@PathVariable Long no, Model model, HttpSession session) {
        ImportBoard importBoard = importBoardService.findById(no);
        if (importBoard == null) {
            model.addAttribute("error", "해당 게시글을 찾을 수 없습니다.");
            return "redirect:/import_board/list";
        }
        importBoard.setViews(importBoard.getViews() + 1); //조회수 1씩 추가
        importBoardService.save(importBoard);

        model.addAttribute("importBoard", importBoard);

        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);
        return "import_board/view";  // 상세보기 템플릿 (view.html)
    }


    // 관리자 전용: 공지사항 작성 페이지 (GET /import_board/list)
    @GetMapping("/new")
    public String showCreateNoticeForm(Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.getId().equals("root")) {
            model.addAttribute("error", "공지사항 작성 권한이 없습니다.");
            return "redirect:/import_board/list"; // 관리자 아니면 목록 페이지로 리다이렉트
        }
        model.addAttribute("importBoard", new ImportBoard());
        return "import_board/create";  // 작성 페이지 템플릿 (예: create.html)
    }

    // 관리자 전용: 공지사항 작성 처리 (POST /import_board/create)
    @PostMapping("/create")
    public String createNotice(@ModelAttribute ImportBoard importBoard, HttpSession session, Model model) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.getId().equals("root")) {
            model.addAttribute("error", "공지사항 작성 권한이 없습니다.");
            return "redirect:/import_board/list"; // 관리자 아니면 목록 페이지로 리다이렉트
        }
        // 두 번째 인수로 사용자 ID(String)를 전달
        importBoardService.createImportBoard(importBoard, currentUser.getId());
        return "redirect:/import_board/list"; // 글 작성 완료 후 목록 페이지로 리다이렉트
    }

    // 수정 페이지 (GET /import_board/edit/{no})
    @GetMapping("/edit/{no}")
    public String showEditNoticeForm(@PathVariable Long no, Model model, HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/import_board/list";
        }

        ImportBoard importBoard = importBoardService.findByNo(no);
        if (importBoard == null) {
            model.addAttribute("error", "존재하지 않는 게시글입니다.");
            return "redirect:/import_board/list";
        }

        // 작성자 본인(또는 관리자)만 수정 가능
        // 필요하다면 관리자도 수정 가능하게 할 수도 있음
        if (!importBoard.getUser().getId().equals(currentUser.getId())) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/import_board/list";
        }

        model.addAttribute("importBoard", importBoard);
        return "import_board/create"; // 같은 폼(template) 사용
    }

    // 수정 처리 (POST /import_board/update/{no})
    @PostMapping("/update/{no}")
    public String updateNotice(@PathVariable Long no,
                               @ModelAttribute ImportBoard importBoard,
                               HttpSession session,
                               Model model) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/import_board/list";
        }

        // 기존 게시글 조회
        ImportBoard existingBoard = importBoardService.findByNo(no);
        if (existingBoard == null) {
            model.addAttribute("error", "존재하지 않는 게시글입니다.");
            return "redirect:/import_board/list";
        }

        // 작성자 본인(또는 관리자)만 수정 가능
        if (!existingBoard.getUser().getId().equals(currentUser.getId())) {
            model.addAttribute("error", "수정 권한이 없습니다.");
            return "redirect:/import_board/list";
        }

        // 실제 수정 로직
        importBoardService.updateImportBoard(existingBoard, importBoard);
        return "redirect:/import_board/list";
    }

    //삭제 처리
    @PostMapping("/delete/{no}")
    public String deleteNotice(@PathVariable Long no, HttpSession session, RedirectAttributes redirectAttributes) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        if (currentUser == null || !currentUser.getRole().equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "공지사항 삭제 권한이 없습니다.");
            return "redirect:/import_board/list";
        }
        try {
            importBoardService.deleteImportBoard(no);
            redirectAttributes.addFlashAttribute("message", "공지사항이 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "공지사항 삭제에 실패했습니다.");
        }
        return "redirect:/import_board/list";
    }

    //검색 매핑 추가
    @GetMapping("/search")
    public String searchNotices(
            @RequestParam String keyword,
            Model model,
            HttpSession session
    ) {
        // 검색 결과
        List<ImportBoard> searchResults = importBoardService.searchNotices(keyword);
        model.addAttribute("importBoards", searchResults);

        // 페이지 정보가 필요 없다면 그냥 검색 결과만 넘겨도 됨
        // 검색 시에도 currentUser 넘겨서 버튼 제어
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");
        model.addAttribute("currentUser", currentUser);

        // 검색 결과를 목록 템플릿에 뿌려주면 돼
        return "import_board/list";
    }

}
