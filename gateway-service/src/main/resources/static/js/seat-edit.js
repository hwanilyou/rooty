const token = localStorage.getItem("token");
const urlParams = new URLSearchParams(window.location.search);
const reservationId = urlParams.get("id");

let selectedSeats = [];

function parseJwt(token) {
    return JSON.parse(atob(token.split('.')[1]));
}

async function loadReservationAndSeats() {
    const payload = parseJwt(token);
    const currentUserId = payload.id;

    const res = await fetch(`/api/bookings/${reservationId}`, {
        headers: { "Authorization": `Bearer ${token}` }
    });
    const reservation = await res.json();

    if (reservation.userId !== currentUserId) {
        alert("해당 예약을 수정할 권한이 없습니다.");
        location.href = "/reservation/user-reservation-list";
        return;
    }

    selectedSeats = [reservation.seatName]; // 기존 선택 좌석
    renderSeats(selectedSeats);
}

function renderSeats(preselected = []) {
    const seatMap = document.getElementById("seat-map");
    seatMap.innerHTML = "";
    const seats = ["1A", "1B", "2A", "2B", "3A"];
    seats.forEach(seat => {
        const seatDiv = document.createElement("div");
        seatDiv.className = "seat";
        seatDiv.textContent = seat;
        seatDiv.dataset.seat = seat;
        if (preselected.includes(seat)) {
            seatDiv.classList.add("selected");
        }
        seatDiv.onclick = () => toggleSeat(seatDiv);
        seatMap.appendChild(seatDiv);
    });
}

function toggleSeat(seatDiv) {
    const seatCode = seatDiv.dataset.seat;
    if (seatDiv.classList.contains("selected")) {
        seatDiv.classList.remove("selected");
        selectedSeats = selectedSeats.filter(s => s !== seatCode);
    } else {
        selectedSeats = [seatCode]; // 수정용이므로 하나만
        document.querySelectorAll('.seat.selected').forEach(s => s.classList.remove("selected"));
        seatDiv.classList.add("selected");
    }
}

function submitUpdatedSeats() {
    if (selectedSeats.length === 0) {
        alert("좌석을 선택하세요.");
        return;
    }

    fetch(`/api/bookings/update-seat`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
            reservationId: reservationId,
            newSeat: selectedSeats[0]
        })
    }).then(() => {
        alert("좌석이 수정되었습니다.");
        location.href = "/reservation/user-reservation-list";
    }).catch(err => {
        console.error(err);
        alert("좌석 수정 실패");
    });
}

loadReservationAndSeats();
