document.addEventListener('DOMContentLoaded', function() {
    const tableBody = document.getElementById('flightTableBody');

    // 1) 백엔드에서 항공편 목록 가져오기
    fetch('/api/flights')
        .then(response => response.json())
        .then(flights => {
            // 2) 기존 정적 데이터 제거
            tableBody.innerHTML = '';

            // 3) 각 항공편마다 테이블 행 생성
            flights.forEach(flight => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
          <td>${flight.id}</td>
          <td>${flight.airline}</td>
          <td>${new Date(flight.departureTime).toLocaleString()}</td>
          <td>${new Date(flight.arrivalTime).toLocaleString()}</td>
          <td>${flight.departureLoc}</td>
          <td>${flight.arrivalLoc}</td>
          <td>
            <a href="/flight/admin-edit/${flight.id}" class="btn small">수정</a>
            <a href="javascript:void(0)" data-flight-id="${flight.id}" class="btn small danger delete-flight">삭제</a>
          </td>
        `;
                tableBody.appendChild(tr);
            });

            // 4) 삭제 버튼 이벤트 바인딩
            document.querySelectorAll('.delete-flight').forEach(btn => {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const id = this.getAttribute('data-flight-id');
                    if (confirm('정말 삭제하시겠습니까?')) {
                        fetch(`/api/flights/${id}`, { method: 'DELETE' })
                            .then(res => {
                                if (res.ok) {
                                    alert('삭제 성공');
                                    // 목록 다시 로드
                                    window.location.reload();
                                } else {
                                    alert('삭제 실패');
                                }
                            })
                            .catch(err => {
                                console.error('삭제 오류:', err);
                                alert('삭제 중 오류 발생');
                            });
                    }
                });
            });
        })
        .catch(err => {
            console.error('항공편 목록 불러오기 오류:', err);
            tableBody.innerHTML = '<tr><td colspan="7">데이터 로드 실패</td></tr>';
        });
});
