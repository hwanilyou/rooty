// ✅ 로그인 상태 및 역할에 따라 네비게이션 메뉴 동적 표시
window.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");

    const loginNav = document.getElementById("loginNav");
    const userNav = document.getElementById("userNav");
    const logoutNav = document.getElementById("logoutNav");
    const usernameSpan = document.getElementById("username");

    const adminMenuItems = document.querySelectorAll(".admin-only");
    const userMenuItems = document.querySelectorAll(".user-only");

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.sub;
            const role = (payload.role || "").toUpperCase(); // ✅ 안전하게 대문자로 변환
            const userId = payload.id; // ✅ 추가된 userId

            console.log("🔐 로그인 사용자:", username, "| 역할:", role); // ✅ 디버깅용
            console.log("✅ 사용자 ID:", userId);
            
            sessionStorage.setItem('userId', userId);
            sessionStorage.setItem('username', username); // 유저 이름도 저장

            // 로그인 상태 UI 처리
            loginNav?.classList.add("d-none");
            userNav?.classList.remove("d-none");
            logoutNav?.classList.remove("d-none");
            if (usernameSpan) usernameSpan.textContent = username;

            if (role === "ADMIN") {
                adminMenuItems.forEach(item => item.classList.remove("d-none"));
                userMenuItems.forEach(item => item.classList.add("d-none")); // ✅ 일반 사용자 메뉴 숨김
            } else {
                userMenuItems.forEach(item => item.classList.remove("d-none"));
                adminMenuItems.forEach(item => item.classList.add("d-none")); // ✅ 관리자 메뉴 숨김
            }

        } catch (e) {
            console.error("❌ JWT 파싱 실패:", e);
            localStorage.removeItem("token");
        }
    }

    // 로그아웃 처리
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function (e) {
            e.preventDefault();
            localStorage.removeItem("token");
            location.href = "/";
        });
    }
});
