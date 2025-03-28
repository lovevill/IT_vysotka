package ru.testPr.test.Service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResultsService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ResultsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void processResultsFile(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheet("Результаты");

        if (sheet == null) {
            throw new IllegalArgumentException("Лист 'Результаты' не найден!");
        }

        int i = 0;
        int team_iter = 0;
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Пропускаем заголовок

            Cell teamCell = row.getCell(0); // Название команды
            Cell pointsCell = row.getCell(2); // Баллы
            Cell placeCell = row.getCell(3); // Место

            // Пропускаем пустые строки
            if (teamCell == null || pointsCell == null || placeCell == null) continue;

            int team, points, place;
            try {
	            team = (int) teamCell.getNumericCellValue();
	            points = (int) pointsCell.getNumericCellValue();
	            place = (int) placeCell.getNumericCellValue();
            }
	        catch(IllegalStateException e) {
	        	continue;
	        }
            
//            System.out.println(team);
//            System.out.println(points);
//            System.out.println(place);
            
            // Вызов функции для обновления БД
            if(team_iter != team) {
            	updateTeamResultsInDatabase(team, points, place, i);
            	team_iter = team;
            }
            i++;
        }
    }

    private void updateTeamResultsInDatabase(int team, int points, int place, int i) {
        String sql = "CALL update_team_results(?, ?, ?, ?)";
        jdbcTemplate.update(sql, team, points, place, i);
    }
}

