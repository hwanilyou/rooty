let selectedSeats = [];
const urlParams = new URLSearchParams(window.location.search);
const flightId = urlParams.get('flightId');
const passengers = urlParams.get('passengers');
const maxSelectableSeats = parseInt(passengers);
console.log('Flight ID:', flightId);
console.log('Passengers:', passengers);

window.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch(`/api/bookings/reserved-seats?flightId=${flightId}`); // ✅ 수정된 경로
        if (!response.ok) throw new Error("예약된 좌석 정보를 불러오는 데 실패했습니다.");

        const reservedSeats = await response.json();
        if (!Array.isArray(reservedSeats)) throw new Error("좌석 정보가 올바르지 않습니다.");

        reservedSeats.forEach(seatCode => {
            const seatElement = document.querySelector(`.seat[data-seat="${seatCode}"]`);
            if (seatElement) {
                seatElement.classList.add('reserved');
            }
        });
    } catch (error) {
        console.error('예약된 좌석 정보를 불러오는 중 오류 발생:', error);
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
});

function confirmBooking() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
        window.location.href = "../login/login.html";
        return;
    }

    if (selectedSeats.length !== maxSelectableSeats) {
        alert(`총 ${maxSelectableSeats}개 좌석을 선택해야 예약이 가능합니다.`);
        return;
    }

    const seatData = selectedSeats.map(seatName => {
        const seatElement = document.querySelector(`.seat[data-seat="${seatName}"]`);
        const seatClass = seatElement.dataset.class;
        const price = getPriceForClass(seatClass);

        return {
            seatName: seatName,
            seatClass: seatClass,
            price: price
        };
    });

    const requestData = {
        userId: getLoggedInUserId(),  // ✅ userId 반드시 포함
        flightId: flightId,
        seats: seatData
    };

    fetch('http://localhost:8000/api/bookings/book-seats', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(requestData)
    })
        .then(response => {
            if (response.ok) {
                alert('예약이 완료되었습니다!');
                window.location.href = '/';
            } else {
                alert('예약 중 오류가 발생했습니다.');
            }
        })
        .catch(error => {
            console.error('예약 요청 중 오류 발생:', error);
            alert('서버 오류로 예약에 실패했습니다.');
        });
}

function getPriceForClass(seatClass) {
    if (seatClass === 'first') return 500;
    if (seatClass === 'business') return 400;
    if (seatClass === 'economy') return 3000;
    return 100;
}

function getLoggedInUserId() {
    const token = localStorage.getItem("token");
    if (!token) return null;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const userId = payload.id;  // ✅ 여기만 수정
        console.log("userId:", userId);
        return userId;
    } catch (e) {
        console.error("토큰 파싱 오류:", e);
        return null;
    }
}

