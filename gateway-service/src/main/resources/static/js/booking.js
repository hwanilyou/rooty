const flightTableBody = document.getElementById('flightTableBody');
const departureSelect = document.getElementById('departureSelect');

let flights = []; // 전체 항공편 데이터

// 항공편 데이터 가져오기
async function fetchFlights() {
    try {
        const response = await fetch('/api/flights'); // Gateway 주소 (상대 경로)
        if (!response.ok) throw new Error('네트워크 응답에 문제가 있습니다.');
        flights = await response.json();
        console.log('📦 불러온 항공편 목록:', flights);
    } catch (error) {
        console.error('항공편 정보를 불러오는 데 실패했습니다:', error);
        flightTableBody.innerHTML = '<tr><td colspan="6">항공편 정보를 불러오는 데 실패했습니다.</td></tr>';
    }
}

// 테이블 렌더링 함수
function renderTable(filteredFlights) {
    flightTableBody.innerHTML = '';

    if (filteredFlights.length === 0) {
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
                    <a href="/seat-selection?flightId=${flight.id}&passengers=${passengers}"
                       class="btn btn-sm btn-primary">좌석 선택</a>
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

    const filteredFlights = flights.filter(flight => flight.departureLoc === selectedDeparture);
    renderTable(filteredFlights);
});

// 인원수 선택 이벤트
document.getElementById('passengers').addEventListener('change', () => {
    const selectedDeparture = departureSelect.value;
    if (!selectedDeparture) return;

    const filteredFlights = flights.filter(flight => flight.departureLoc === selectedDeparture);
    renderTable(filteredFlights);
});

// 페이지 로드 시 데이터 가져오기
document.addEventListener('DOMContentLoaded', async () => {
    await fetchFlights();
    flightTableBody.innerHTML = '<tr><td colspan="6">출발지를 선택하세요.</td></tr>';
});

// 푸터 불러오기
fetch('/fragments/footer.html')
    .then(res => res.text())
    .then(html => {
        document.querySelector('#footer-container').innerHTML = html;
    });
