
    let selectedSeats = [];
    const urlParams = new URLSearchParams(window.location.search);
    const flightId = urlParams.get('id');
    const passengers = urlParams.get('passengers');
    const maxSelectableSeats = parseInt(urlParams.get('passengers'));
    console.log('Flight ID:', flightId);
    console.log('Passengers:', passengers);

    window.addEventListener('DOMContentLoaded', async () => {
    try {
    const response = await fetch(`/api/reserved-seats?flightId=${flightId}`); // 예약된 좌석 정보 API
    const reservedSeats = await response.json(); // 예: ["A1", "B2", "C3"]

    reservedSeats.forEach(seatCode => {
    const seatElement = document.querySelector(`.seat[data-seat="${seatCode}"]`);
    if (seatElement) {
    seatElement.classList.add('reserved');
}
});
} catch (error) {
    console.error('예약된 좌석 정보를 불러오는 중 오류 발생:', error);
}

    // 좌석 클릭 이벤트 바인딩 (DOMContentLoaded 안으로 옮기면 초기화 문제 없음)
    document.querySelectorAll('.seat').forEach(seat => {
    seat.addEventListener('click', () => {
    if (seat.classList.contains('reserved')) return; // 이미 예약된 좌석 클릭 방지

    const seatCode = seat.getAttribute('data-seat');

    if (seat.classList.contains('selected')) {
    // 선택 해제
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
});

    function confirmBooking() {
    if (selectedSeats.length !== maxSelectableSeats) {
    alert(`총 ${maxSelectableSeats}개 좌석을 선택해야 예약이 가능합니다.`);
    return;
}

    // 선택된 좌석 서버로 전송
    fetch('/api/book-seats', {
    method: 'POST',
    headers: {
    'Content-Type': 'application/json'
},
    body: JSON.stringify({seats: selectedSeats})
})
    .then(response => {
    if (response.ok) {
    alert('예약이 완료되었습니다!');
    window.location.href = '/booking-success.html'; // 성공 페이지로 이동
} else {
    alert('예약 중 오류가 발생했습니다.');
}
})
    .catch(error => {
    console.error('예약 요청 중 오류 발생:', error);
    alert('서버 오류로 예약에 실패했습니다.');
});
}
