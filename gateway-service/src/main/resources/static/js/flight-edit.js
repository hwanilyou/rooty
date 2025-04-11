document.addEventListener('DOMContentLoaded', () => {
    // 1) URL에서 ID 추출
    const id = window.location.pathname.split('/').pop();
    document.getElementById('id').value = id;

    // 2) 기존 데이터 로드
    fetch(`/api/flights/${id}`)
        .then(res => res.json())
        .then(flight => {
            document.getElementById('airline').value = flight.airline;
            document.getElementById('departureTime').value = flight.departureTime.slice(0,16);
            // 자동 도착 시간
            const d = new Date(flight.departureTime);
            d.setHours(d.getHours()+1);
            document.getElementById('arrivalTime').value = d.toISOString().slice(0,16);
            document.getElementById('departureLoc').value = flight.departureLoc;
            document.getElementById('arrivalLoc').value = flight.arrivalLoc;
        });

    // 3) 폼 제출 시 PUT 호출
    document.getElementById('flightEditForm').addEventListener('submit', e => {
        e.preventDefault();
        const flightData = {
            airline: document.getElementById('airline').value,
            departureTime: document.getElementById('departureTime').value,
            arrivalTime: document.getElementById('arrivalTime').value,
            departureLoc: document.getElementById('departureLoc').value,
            arrivalLoc: document.getElementById('arrivalLoc').value
        };
        fetch(`/api/flights/${id}`, {
            method: 'PUT',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify(flightData)
        })
            .then(r => {
                if (r.ok) {
                    alert('수정 성공');
                    location.href = '/flight/admin-list';
                } else {
                    alert('수정 실패');
                }
            })
            .catch(err => {
                console.error(err);
                alert('수정 중 오류');
            });
    });
});
