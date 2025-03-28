let searchTimeout; // Таймер для отслеживания ввода
	
function searchParticipantsDebounced() {
    const query = document.getElementById('query').value;
	//const dateId = document.getElementById('date-filter').value;
    const schoolId = document.getElementById('school-filter').value;
    const audienceNumber = document.getElementById('audience-filter').value;

    // Если уже есть таймер, сбрасываем его
    if (searchTimeout) {
        clearTimeout(searchTimeout);
    }

    // Устанавливаем новый таймер (например, 300 мс)
    searchTimeout = setTimeout(() => {
    	searchParticipants(query, schoolId, audienceNumber);
    }, 300);
}

// Функции для поиска и отображения участников
function searchParticipants(query, schoolId, audienceNumber) {

    if (query.trim() === "") {
        clearResults();
        return;
    }
	
	/*if(dateId === ""){
		dateId = null;	
	}
	if(schoolId === ""){
		schoolId = null;	
	}
	if(audienceNumber === ""){
		audienceNumber = null;	
	}*/
	
	const url = new URL(`/api/participants/search?query=${encodeURIComponent(query)}`, window.location.origin);
	if (schoolId) url.searchParams.append('schoolId', schoolId);
	if (audienceNumber) url.searchParams.append('audienceNumber', audienceNumber);

    fetch(url.toString())
        .then(response => response.json())
        .then(data => {
            clearResults();

            const tableBody = document.getElementById('results-table').getElementsByTagName('tbody')[0];

            if (data.length > 0) {
                data.forEach(participant => {
                    const row = tableBody.insertRow();
                    row.onclick = () => handleParticipantClick(participant.id);
                    row.insertCell(0).textContent = participant.lastName;
                    row.insertCell(1).textContent = participant.name;
                    row.insertCell(2).textContent = participant.middleName;
                    row.insertCell(3).textContent = participant.schoolName;
                    row.insertCell(4).textContent = participant.teamName;
                    row.style.cursor = "pointer";
                });
            } else {
                const row = tableBody.insertRow();
                const cell = row.insertCell(0);
                cell.colSpan = 5;
                cell.textContent = "Нет результатов";
            }
        })
        .catch(error => console.error("Ошибка при поиске:", error));
}

function clearResults() {
    const tableBody = document.getElementById('results-table').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = "";
}

function handleParticipantClick(participantId) {
    fetch(`/api/participants/${participantId}`)
        .then(response => response.json())
        .then(participant => {
            const details = `
                <p><strong>Фамилия:</strong> ${participant.lastName}</p>
                <p><strong>Имя:</strong> ${participant.name}</p>
                <p><strong>Отчество:</strong> ${participant.middleName}</p>
                <p><strong>Школа:</strong> ${participant.schoolName}</p>
                <p><strong>Команда:</strong> ${participant.teamName}</p>
                <p><strong>Телефон:</strong> ${participant.phoneNumber}</p>
                <p><strong>Email:</strong> ${participant.email}</p>
                <p><strong>Класс:</strong> ${participant.schoolClass}</p>
                <p><strong>Аудитория:</strong> ${participant.audience ? participant.audience : "Не указано"}</p>
                <p><strong>Номер компьютера:</strong> ${participant.computerNumber ? participant.computerNumber : "Не указано"}</p>
                <p><strong>Явка:</strong> ${participant.attended === true ? "Да" : "Нет"}</p>
            `;
            document.getElementById('participantDetails').innerHTML = details;
            
            const attendanceButton = document.getElementById('attendanceButton');
            if (participant.attended === true) {
                attendanceButton.innerText = "Убрать явку";
                attendanceButton.onclick = () => updateAttendance(participantId, false);
            } else {
                attendanceButton.innerText = "Явился";
                attendanceButton.onclick = () => updateAttendance(participantId, true);
            }
            
            openModal();
        })
        .catch(error => console.error("Ошибка при получении информации об участнике:", error));
}

// Открытие и закрытие модального окна
function openModal() {
    document.getElementById('participantModal').style.display = 'flex';
}

function closeModal() {
    document.getElementById('participantModal').style.display = 'none';
}

function updateAttendance(participantId, status) {
    fetch(`/api/participants/${participantId}/attendance`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ attended: status }),
    })
        .then(response => {
            if (response.ok) {
                // Перезагрузка данных участника после обновления
                handleParticipantClick(participantId);
            } else {
                console.error("Ошибка при обновлении статуса явки");
            }
        })
        .catch(error => console.error("Ошибка:", error));
}

let qrCodeScanner; // Объявляем глобально, чтобы использовать объект повторно

function openQrScanner() {
    const modal = document.getElementById('qrScannerModal');
    modal.style.display = 'block';

    if (!qrCodeScanner) {
        qrCodeScanner = new Html5Qrcode("qr-reader"); // Инициализация сканера
    }

    // Запускаем камеру
    qrCodeScanner.start(
        { facingMode: "environment" }, // Используем основную камеру
        { fps: 10, qrbox: { width: 250, height: 250 } },
        (decodedText) => {
            closeQrScanner();
            console.log("QR-код:", decodedText);

            // Предполагаем, что QR-код содержит ID участника
            const participantId = decodedText.split('=')[1]; // Извлекаем ID
            if (participantId) {
                handleParticipantClick(participantId);
            }
        },
        (errorMessage) => {
            console.warn("Ошибка сканирования:", errorMessage);
        }
    ).catch((err) => {
        console.error("Ошибка при запуске камеры:", err);
        alert("Не удалось открыть камеру. Проверьте разрешения.");
        closeQrScanner();
    });
}

function closeQrScanner() {
    const modal = document.getElementById('qrScannerModal');
    modal.style.display = 'none';

    if (qrCodeScanner) {
        qrCodeScanner.stop().catch(err => {
            console.error("Ошибка при остановке камеры:", err);
        });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const participantId = urlParams.get('id');

    if (participantId) {
        handleParticipantClick(participantId); // Загружаем данные участника
    }
	
    loadFilters();
});

async function loadFilters() {
    try {
        const response = await fetch('/api/filters'); // Эндпоинт для получения данных фильтров
        const filters = await response.json();

        //populateFilter('date-filter', filters.dates);
        populateFilter('school-filter', filters.schools);
        populateFilter('audience-filter', filters.audiences);
    } catch (error) {
        console.error('Ошибка загрузки фильтров:', error);
    }
}

function populateFilter(filterId, options) {
    const filterElement = document.getElementById(filterId);
    options.forEach(option => {
        const opt = document.createElement('option');
        opt.value = option.value;
        opt.textContent = option.label;
        filterElement.appendChild(opt);
    });
}