const flightTableBody = document.getElementById('flightTableBody');
const departureSelect = document.getElementById('departureSelect');
let flights = []; // 전체 항공편 데이터

// 항공편 데이터 가져오기
async function fetchFlights() {
    try {
        const response = await fetch('/api/flights'); // ✅ 수정됨: 상대 경로
        if (!response.ok) throw new Error('네트워크 응답에 문제가 있습니다.');
        flights = await response.json();
        console.log('📦 불러온 항공편 목록:', flights);
    } catch (error) {
        console.error('❌ 항공편 정보를 불러오는 데 실패했습니다:', error);
        flightTableBody.innerHTML = '<tr><td colspan="6">항공편 정보를 불러오는 데 실패했습니다.</td></tr>';
    }
}

// 테이블 렌더링 함수
function renderTable(filteredFlights) {
    flightTableBody.innerHTML = '';

    if (!filteredFlights || filteredFlights.length === 0) {
        flightTableBody.innerHTML = '<tr><td colspan="6">선택한 출발지에 대한 항공편이 없습니다.</td></tr>';
        return;
    }

    const passengers = document.getElementById('passengers').value;

    filteredFlights.forEach(flight => {
        const row = `
            <tr>
                <td>${flight.departureLoc}</td>
                <td>${flight.arrivalLoc}</td>
                <td>${flight.airline}</td>
                <td>${flight.departureTime}</td>
                <td>${flight.arrivalTime}</td>
                <td>
                    <a href="/seat-selection?id=${flight.id}&passengers=${passengers}" class="btn btn-sm btn-primary">좌석 선택</a>
                </td>
            </tr>`;
        flightTableBody.insertAdjacentHTML('beforeend', row);
    });
}

// 출발지 선택 이벤트
departureSelect.addEventListener('change', () => {
    const selectedDeparture = departureSelect.value;
    if (!selectedDeparture) {
        flightTableBody.innerHTML = '<tr><td colspan="6">출발지를 선택하세요.</td></tr>';
        return;
    }

    const filteredFlights = flights.filter(f => f.departureLoc === selectedDeparture);
    renderTable(filteredFlights);
});

// 인원수 변경 이벤트
document.getElementById('passengers').addEventListener('change', () => {
    const selectedDeparture = departureSelect.value;
    if (!selectedDeparture) return;

    const filteredFlights = flights.filter(f => f.departureLoc === selectedDeparture);
    renderTable(filteredFlights);
});

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', async () => {
    await fetchFlights();
    flightTableBody.innerHTML = '<tr><td colspan="6">출발지를 선택하세요.</td></tr>';
});

// ✅ 헤더/푸터 불러오기
fetch('/fragments/header.html')
    .then(res => res.text())
    .then(html => {
        const headerEl = document.querySelector('#header-container');
        if (headerEl) headerEl.innerHTML = html;
    });

fetch('/fragments/footer.html')
    .then(res => res.text())
    .then(html => {
        const footerEl = document.querySelector('#footer-container');
        if (footerEl) footerEl.innerHTML = html;
    });

// ====================== 좌석 선택 페이지 관련 ======================

let selectedSeats = [];
const urlParams = new URLSearchParams(window.location.search);
const flightId = urlParams.get('id');
const passengers = urlParams.get('passengers');
const maxSelectableSeats = parseInt(passengers);
console.log('Flight ID:', flightId);
console.log('Passengers:', passengers);

window.addEventListener('DOMContentLoaded', async () => {
    if (!flightId) {
        console.warn("⚠️ flightId가 null입니다. 좌석 조회를 생략합니다.");
        return;
    }

    try {
        const response = await fetch(`/api/seats/reserved-seats?flightId=${flightId}`); // ✅ 수정됨
        if (!response.ok) throw new Error("좌석 정보를 불러오는 데 실패");

        const reservedSeats = await response.json();
        console.log('예약된 좌석:', reservedSeats);

        if (Array.isArray(reservedSeats)) { // ✅ 배열 체크 추가
            reservedSeats.forEach(seatCode => {
                const seatElement = document.querySelector(`.seat[data-seat="${seatCode}"]`);
                if (seatElement) {
                    seatElement.classList.add('reserved');
                }
            });
        } else {
            console.warn("❗reservedSeats가 배열이 아님:", reservedSeats);
        }

        document.querySelectorAll('.seat').forEach(seat => {
            seat.addEventListener('click', () => {
                if (seat.classList.contains('reserved')) return;

                const seatCode = seat.getAttribute('data-seat');
                if (seat.classList.contains('selected')) {
                    seat.classList.remove('selected');
                    selectedSeats = selectedSeats.filter(s => s !== seatCode);
                } else {
                    if (selectedSeats.length >= maxSelectableSeats) {
                        alert(`최대 ${maxSelectableSeats}개 좌석만 선택할 수 있습니다.`);
                        return;
                    }
                    seat.classList.add('selected');
                    selectedSeats.push(seatCode);
                }

                console.log('선택된 좌석:', selectedSeats);
            });
        });

    } catch (error) {
        console.error('예약된 좌석 정보를 불러오는 중 오류 발생:', error);
    }
});

function confirmBooking() {
    if (selectedSeats.length !== maxSelectableSeats) {
        alert(`총 ${maxSelectableSeats}개 좌석을 선택해야 예약이 가능합니다.`);
        return;
    }

    fetch('/api/seats/book-seats', { // ✅ 수정됨
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ seats: selectedSeats })
    })
        .then(response => {
            if (response.ok) {
                alert('예약이 완료되었습니다!');
                window.location.href = '/booking-success.html';
            } else {
                alert('예약 중 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('예약 요청 중 오류 발생:', error);
            alert('서버 오류로 예약에 실패했습니다.');
        });
}
