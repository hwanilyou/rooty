/* common.js: 프로젝트 공통 기능 */

/**
 * 항공편 삭제 함수
 * @param {number} id - 삭제할 항공편의 ID
 */
function deleteFlight(id) {
    if (confirm("정말 삭제하시겠습니까?")) {
        window.location.href = "/deleteFlight?id=" + id;
    }
}

/**
 * 날짜 객체를 datetime-local 입력에 맞는 문자열로 포맷합니다.
 * @param {Date} dateObj - 날짜 객체
 * @returns {string} formatted - "YYYY-MM-DDTHH:mm" 형식의 문자열
 */
function formatDateForInput(dateObj) {
    var year = dateObj.getFullYear();
    var month = ('0' + (dateObj.getMonth() + 1)).slice(-2);
    var day = ('0' + dateObj.getDate()).slice(-2);
    var hours = ('0' + dateObj.getHours()).slice(-2);
    var minutes = ('0' + dateObj.getMinutes()).slice(-2);
    return year + '-' + month + '-' + day + 'T' + hours + ':' + minutes;
}

// departureTime change 시 arrivalTime 자동 계산 1시간 추가 ㅇ제발
document.addEventListener('DOMContentLoaded', function() {
    const dep = document.getElementById('departureTime');
    const arr = document.getElementById('arrivalTime');
    if (dep && arr) {
        dep.addEventListener('change', () => {
            const d = new Date(dep.value);
            if (!isNaN(d)) {
                const a = new Date(d.getTime() + 3600*1000);
                arr.value = formatDateForInput(a);
            }
        });
    }
});
