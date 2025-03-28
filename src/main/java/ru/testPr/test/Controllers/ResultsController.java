package ru.testPr.test.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ru.testPr.test.Service.ResultsService;

@RestController
@RequestMapping("/upload-report")
public class ResultsController {

    private final ResultsService resultsService;

    @Autowired
    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }

    @PostMapping
    public ResponseEntity<String> uploadResults(@RequestParam("file") MultipartFile file) {
        try {
            resultsService.processResultsFile(file);
            return ResponseEntity.ok("Данные успешно загружены!");
        } catch (DataAccessException e) {
            String errorMessage = extractCustomErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка обработки файла: " + e.getMessage());
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
}

