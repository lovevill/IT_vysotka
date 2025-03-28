package ru.testPr.test.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.testPr.test.Service.AudienceService;

@RestController
@RequestMapping("/api/audience")
public class AudienceController {

    @Autowired
    private AudienceService audienceService;
    
    @GetMapping
    public List<Map<String, Object>> getAllAudiences(){
    	return audienceService.getAllAudiences();
    }

    @PostMapping
    public ResponseEntity<String> addAudienceAndComputers(@RequestBody AudienceRequest request) {
        try {
            audienceService.addAudienceAndComputers(request.getAudienceNumber(), request.getComputerCount(), request.getPrioritet());
            return ResponseEntity.ok("Аудитория и компьютеры успешно добавлены!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataAccessException e) {
            String errorMessage = extractCustomErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при добавлении аудитории и компьютеров: " + e.getMessage());
        }
    }
    
    @PostMapping("/change")
    public ResponseEntity<String> changeAudienceAndComputers(@RequestBody AudienceRequest request) {
        try {
            audienceService.changeAudienceAndComputers(request.getAudienceNumber(), request.getComputerCount(), request.getPrioritet());
            return ResponseEntity.ok("Аудитория успешно изменена!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataAccessException e) {
            String errorMessage = extractCustomErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка при изменении аудитории: " + e.getMessage());
        }
    }
    
    private String extractCustomErrorMessage(String fullMessage) {
        // Ищем текст после "ERROR: "
        int index = fullMessage.indexOf("ERROR:");
        if (index != -1) {
            String errorPart = fullMessage.substring(index + 6).trim();
            int endIndex = errorPart.indexOf("\n");
            return endIndex != -1 ? errorPart.substring(0, endIndex) : errorPart;
        }
        return "Произошла неизвестная ошибка";
    }

    // DTO для запроса
    static class AudienceRequest {
        private int audienceNumber;
        private Integer computerCount;
        private Integer prioritet;

        public int getAudienceNumber() {
            return audienceNumber;
        }

        public void setAudienceNumber(int audienceNumber) {
            this.audienceNumber = audienceNumber;
        }

        public Integer getComputerCount() {
            return computerCount;
        }

        public void setComputerCount(Integer computerCount) {
            this.computerCount = computerCount;
        }
        
        public Integer getPrioritet() {
            return prioritet;
        }

        public void setPrioritet(Integer prioritet) {
            this.prioritet = prioritet;
        }
    }
}


