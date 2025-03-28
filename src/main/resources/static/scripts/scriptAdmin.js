document.addEventListener('DOMContentLoaded', () => {
    const menuButtons = document.querySelectorAll('#admin-menu .btn');
    const activeArea = document.getElementById('active-area');

    // Функция для загрузки содержимого раздела "Компьютеры"
    function loadComputersContent() {
        activeArea.innerHTML = `
            <div id="computers-content" class="active-content">
                <h3>Управление аудиториями</h3>
                <button class="btn btn-success mb-3" id="add-computer-btn">Добавить аудиторию</button>
				<button class="btn btn-success mb-3" id="change-computer-btn">Изменить аудиторию</button>

                <!-- Модальное окно -->
                <div id="add-computer-modal" class="modal" style="display: none;">
                    <div class="modal-content">
                        <span class="close-btn">&times;</span>
                        <h4>Добавить аудиторию</h4>
                        <form id="add-computer-form">
                            <div class="form-group">
                                <label for="audience-number">Номер аудитории:</label>
                                <input type="number" id="audience-number" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label for="computer-count">Количество компьютеров:</label>
                                <input type="number" id="computer-count" class="form-control" required>
                            </div>
							<div class="form-group">
                                <label for="prioritet">Приоритет:</label>
                                <input type="number" id="prioritet" class="form-control">
                            </div>
                            <button type="submit" class="btn btn-primary mt-3">Сохранить</button>
                        </form>
                    </div>
                </div>
				
				<!-- Модальное окно -->
				<div id="change-computer-modal" class="modal" style="display: none;">
				    <div class="modal-content">
				        <span class="close-btn">&times;</span>
				        <h4>Изменить аудиторию</h4>
				        <form id="change-computer-form">
				            <div class="form-group">
				                <label for="audience-number_ch">Номер аудитории:</label>
				                <select id="audience-number_ch" class="form-control" required>
				                    <!-- Список будет заполняться из базы данных -->
				                </select>
				            </div>
				            <div class="form-group">
				                <label for="computer-count_ch">Количество компьютеров:</label>
				                <input type="number" id="computer-count_ch" class="form-control">
				            </div>
				            <div class="form-group">
				                <label for="prioritet_ch">Приоритет:</label>
				                <input type="number" id="prioritet_ch" class="form-control">
				            </div>
				            <button type="submit_change" class="btn btn-primary mt-3">Сохранить</button>
				        </form>
				    </div>
				</div>
            </div>`;
    }

	function loadAudience() {
	    const audienceSelect = document.getElementById('audience-number_ch');

	    // Получение данных с сервера
	    fetch('/api/audience')
	        .then(response => response.json())
	        .then(data => {
	            // Очистка текущих опций
	            audienceSelect.innerHTML = '';

	            // Заполнение списка опциями
	            data.forEach(item => {
	                const option = document.createElement('option');
	                option.value = item.audienceNumber; // Значение из базы
	                option.textContent = item.audienceNumber; // Отображаемый текст
	                audienceSelect.appendChild(option);
	            });
	        })
	        .catch(error => console.error('Ошибка загрузки данных:', error));
	}
	
    // Функция для загрузки содержимого раздела "Распределение"
    function loadDistributionContent() {
        activeArea.innerHTML = `
            <div id="distribution-content" class="active-content">
                <h3>Распределение команд</h3>
                <button class="btn btn-success mb-3" id="distribute-btn">Распределить</button>
                <div id="report" style="margin-top: 20px;"></div> <!-- Контейнер для отчета -->
				
				<button class="btn btn-success mb-3" id="download-report-btn" style="position: absolute; bottom: 20px; left: 20px;">Скачать отчет</button>
				<button class="btn btn-success mb-3" id="send-emails" style="position: absolute; bottom: 20px; left: 150px;">Рассылка</button>
				<button class="btn btn-success mb-3" id="upload-report-btn" style="position: absolute; bottom: 20px; left: 245px;">Загрузить отчет</button>
            </div>`;

			// Делегирование событий для динамически добавляемых элементов
		    document.addEventListener('click', async (event) => {
		        // Обработчик для кнопки "Распределить"
		        if (event.target && event.target.id === 'distribute-btn') {
		            const reportSection = document.getElementById('report');

		            // Показываем спиннер
		            const loadingOverlay = document.getElementById('loading-overlay');
		            loadingOverlay.style.display = 'flex';

		            fetch('/api/distribute-teams/distribute', { method: 'GET' })
		                .then(response => response.json())
		                .then(data => {
		                    reportSection.innerHTML = `
		                        <div class="report">
		                            <h4>Отчет</h4>
		                            <p>Всего команд для распределения: ${data.totalTeams}</p>
		                            <p>Распределено команд: ${data.allocatedTeams}</p>
		                            <p>Нераспределено команд: ${data.unallocatedTeams}</p>
		                            <p>Занято аудиторий: ${data.occupiedAudiences}</p>
		                            <p>Назначено кураторов: ${data.assignedCurators}</p>
		                            <p>Статус: ${data.status}</p>
		                        </div>`;
		                })
		                .catch(error => {
		                    reportSection.innerHTML = '<p class="text-danger">Ошибка при распределении команд. Попробуйте снова.</p>';
		                    console.error('Error:', error);
		                })
		                .finally(() => {
		                    loadingOverlay.style.display = 'none';
		                });
		        }

		        // Обработчик для кнопки "Скачать отчет"
		        if (event.target && event.target.id === 'download-report-btn') {
					const loadingOverlay = document.getElementById('loading-overlay');
		            loadingOverlay.style.display = 'flex';
					try {
			            const response = await fetch('/api/report/generate', {
			                method: 'GET',
			            });

			            if (response.ok) {
			                const blob = await response.blob();
			                const link = document.createElement('a');
			                link.href = window.URL.createObjectURL(blob);
			                link.download = 'report.xlsx';
			                link.click();
			                alert('Отчет успешно создан!');
			            } else {
			                alert('Не удалось создать отчет.');
			            }
			        } catch (error) {
			            console.error(error);
			            alert('Произошла ошибка при создании отчета.');
			        }
					finally{
						loadingOverlay.style.display = 'none';
					}
		        }

				if (event.target && event.target.id === 'send-emails') {
					// Деактивируем кнопку, чтобы избежать многократных кликов
				    document.getElementById('send-emails').disabled = true;

				    // Отправляем запрос на сервер для начала рассылки
				    fetch('/api/send-emails', {
				      method: 'POST',
				    })
				    .then(response => response.json())
				    .then(data => {
				      // После успешной рассылки показываем уведомление
				      alert('Все письма отправлены!');
				      // Включаем кнопку снова, если нужно
				      document.getElementById('send-emails').disabled = false;
				    })
				    .catch(error => {
				      console.error('Ошибка:', error);
				      document.getElementById('send-emails').disabled = false;  // Включаем кнопку обратно в случае ошибки
				    });
		        }
				
				if (event.target && event.target.id === 'upload-report-btn') {
					const input = document.createElement("input");
				    input.type = "file";
				    input.accept = ".xlsx, .xls";
				    
				    input.onchange = async (event) => {
				        const file = event.target.files[0];
				        if (file) {
				            const formData = new FormData();
				            formData.append("file", file);
				            
				            try {
				                const response = await fetch("/upload-report", {
				                    method: "POST",
				                    body: formData
				                });
				                
				                const result = await response.text();
				                alert(result); // Показываем результат загрузки
				            } catch (error) {
				                alert("Ошибка загрузки файла: " + error.message);
				            }
				        }
				    };
				    
				    input.click();
				}
		    });
    }

    // Обработчик переключения кнопок в меню
    menuButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Удалить активный класс у всех кнопок
            menuButtons.forEach(btn => btn.classList.remove('btn-primary'));
            menuButtons.forEach(btn => btn.classList.add('btn-outline-primary'));

            // Сделать текущую кнопку активной
            button.classList.remove('btn-outline-primary');
            button.classList.add('btn-primary');

            // Очистить активную область
            activeArea.innerHTML = '';

            // Загрузить соответствующий контент
            if (button.id === 'btn-computers') {
                loadComputersContent();
				loadAudience();
            } else if (button.id === 'btn-distribute') {
                loadDistributionContent();
            }
        });
    });

    // Делегирование событий для динамически добавляемых элементов
    document.addEventListener('click', (event) => {
        // Обработчик для кнопки "Добавить компьютер"
        if (event.target && event.target.id === 'add-computer-btn') {
            const addComputerModal = document.getElementById('add-computer-modal');
            addComputerModal.style.display = 'flex';
        }

        // Обработчик для закрытия модального окна
        if (event.target && event.target.classList.contains('close-btn')) {
            const modal = event.target.closest('.modal');
            if (modal) modal.style.display = 'none';
        }
		
		if (event.target && event.target.id === 'change-computer-btn') {
            const changeComputerModal = document.getElementById('change-computer-modal');
            changeComputerModal.style.display = 'flex';
        }
    });

    // Обработка формы добавления компьютера
    document.addEventListener('submit', async (event) => {
        if (event.target && event.target.id === 'add-computer-form') {
            event.preventDefault();

            const saveButton = event.target.querySelector('button[type="submit"]');
            saveButton.disabled = true;

            // Показываем спиннер
            const loadingOverlay = document.getElementById('loading-overlay');
            loadingOverlay.style.display = 'flex';

            try {
                // Получаем данные из формы
                const audienceNumber = document.getElementById('audience-number').value;
                const computerCount = document.getElementById('computer-count').value;
				const prioritet = document.getElementById('prioritet').value || null;
				
                // Проверка валидности данных
                if (!audienceNumber || !computerCount) {
                    throw new Error('Пожалуйста, заполните все поля.');
                }
				
				if(parseInt(computerCount, 10) <= 0 || parseInt(audienceNumber, 10) <= 0){
					throw new Error('Пожалуйста, введите допустимое значение.');
				}
                // Отправляем данные на сервер
                const response = await fetch('/api/audience', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        audienceNumber: parseInt(audienceNumber, 10),
                        computerCount: parseInt(computerCount, 10),
						prioritet: prioritet ? parseInt(prioritet, 10): null,
                    }),
                });

                if (!response.ok) {
					return response.text().then(errorMessage => {
	                    alert(`Ошибка: ${errorMessage}`); // Показываем ошибку через alert
	                });;
                }

                // Успешное завершение
                alert('Аудитория и компьютеры успешно добавлены!');
                document.getElementById('add-computer-modal').style.display = "none";
            } catch (error) {
                // Обработка ошибок
                alert(error.message);
            } finally {
                // Скрываем спиннер и включаем кнопку
				loadAudience();
                loadingOverlay.style.display = 'none';
                saveButton.disabled = false;
            }
        }
		
		if (event.target && event.target.id === 'change-computer-form') {
	            event.preventDefault();

	            const saveButton = event.target.querySelector('button[type="submit_change"]');
	            saveButton.disabled = true;

	            // Показываем спиннер
	            const loadingOverlay = document.getElementById('loading-overlay');
	            loadingOverlay.style.display = 'flex';

	            try {
	                // Получаем данные из формы
	                const audienceNumber = document.getElementById('audience-number_ch').value;
	                const computerCount = document.getElementById('computer-count_ch').value || null;
					const prioritet = document.getElementById('prioritet_ch').value || null;
					
	                // Проверка валидности данных
	                if (!audienceNumber) {
	                    throw new Error('Пожалуйста, заполните все поля.');
	                }
					
					if(parseInt(audienceNumber, 10) <= 0){
						throw new Error('Пожалуйста, введите допустимое значение.');
					}
	                // Отправляем данные на сервер
	                const response = await fetch('/api/audience/change', {
	                    method: 'POST',
	                    headers: {
	                        'Content-Type': 'application/json',
	                    },
	                    body: JSON.stringify({
	                        audienceNumber: parseInt(audienceNumber, 10),
	                        computerCount: computerCount ? parseInt(computerCount, 10) : null,
							prioritet: prioritet ? parseInt(prioritet, 10) : null,
	                    }),
	                });

	                if (!response.ok) {
						return response.text().then(errorMessage => {
		                    alert(`Ошибка: ${errorMessage}`); // Показываем ошибку через alert
		                });;
	                }

	                // Успешное завершение
	                alert('Аудитория успешно изменена!');
	                document.getElementById('add-computer-modal').style.display = "none";
	            } catch (error) {
	                // Обработка ошибок
	                alert(error.message);
	            } finally {
	                // Скрываем спиннер и включаем кнопку
	                loadingOverlay.style.display = 'none';
	                saveButton.disabled = false;
	            }
	        }
    });
	
	/*document.addEventListener('submit_change', async (event) => {
        if (event.target && event.target.id === 'change-computer-form') {
            event.preventDefault();

            const saveButton = event.target.querySelector('button[type="submit_change"]');
            saveButton.disabled = true;

            // Показываем спиннер
            const loadingOverlay = document.getElementById('loading-overlay');
            loadingOverlay.style.display = 'flex';

            try {
                // Получаем данные из формы
                const audienceNumber = document.getElementById('audience-number_ch').value;
                const computerCount = document.getElementById('computer-count_ch').value || null;
				const prioritet = document.getElementById('prioritet_ch').value || null;
				
                // Проверка валидности данных
                if (!audienceNumber) {
                    throw new Error('Пожалуйста, заполните все поля.');
                }
				
				if(parseInt(audienceNumber, 10) <= 0){
					throw new Error('Пожалуйста, введите допустимое значение.');
				}
                // Отправляем данные на сервер
                const response = await fetch('/api/audience/change', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        audienceNumber: parseInt(audienceNumber, 10),
                        computerCount: computerCount ? parseInt(computerCount, 10) : null,
						prioritet: prioritet ? parseInt(prioritet, 10) : null,
                    }),
                });

                if (!response.ok) {
					return response.text().then(errorMessage => {
	                    alert(`Ошибка: ${errorMessage}`); // Показываем ошибку через alert
	                });;
                }

                // Успешное завершение
                alert('Аудитория успешно изменена!');
                document.getElementById('add-computer-modal').style.display = "none";
            } catch (error) {
                // Обработка ошибок
                alert(error.message);
            } finally {
                // Скрываем спиннер и включаем кнопку
                loadingOverlay.style.display = 'none';
                saveButton.disabled = false;
            }
        }
    });*/

    // Нажатие на первую кнопку по умолчанию
    document.getElementById('btn-computers').click();
});
