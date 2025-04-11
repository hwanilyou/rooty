document.addEventListener('DOMContentLoaded', function() {
    const flightForm = document.getElementById('flightRegisterForm');
    if (flightForm) {
        flightForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const flightData = {
                airline: document.getElementById('airline').value,
                departureTime: document.getElementById('departureTime').value,
                arrivalTime: document.getElementById('arrivalTime').value,
                departureLoc: document.getElementById('departureLoc').value,
                arrivalLoc: document.getElementById('arrivalLoc').value
            };

            fetch('/api/flights', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(flightData)
            })
                .then(response => {
                    if (response.ok) {
                        alert('항공편 등록 성공');
                        window.location.href = '/flight/admin-flight-list';
                    } else {
                        response.text().then(text => {
                            alert('등록 실패: ' + text);
                        });
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('등록 중 오류 발생');
                });
        });
    }
});
