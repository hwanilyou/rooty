document.addEventListener("DOMContentLoaded", function () {
    // 캐러셀 자동 슬라이드 설정 (5초마다 슬라이드)
    let carousel = new bootstrap.Carousel(document.getElementById("carouselExample"), {
        interval: 5000, // 5초마다 슬라이드 변경
        ride: "carousel"
    });
});

// 공지사항 섹션으로 스크롤 시 캐러셀도 같이 올라가게 설정
function scrollToNotice() {
    window.scrollTo({
        top: document.getElementById("notice-section").offsetTop - 60, // 네비게이션 바 높이 고려
        behavior: "smooth"
    });
}