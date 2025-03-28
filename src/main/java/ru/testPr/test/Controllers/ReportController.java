package ru.testPr.test.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import ru.testPr.test.Service.ExcelReportGenerator;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ExcelReportGenerator reportGenerator;

    @Autowired
    public ReportController(ExcelReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @GetMapping("/generate")
    public ResponseEntity<Resource> generateReport() {
        String filePath = "report.xlsx"; // Временное расположение файла
        List<String> tableNames = List.of("team", "audience", "computers", "curators", "participant", "school");

        try {
            reportGenerator.generateReport(filePath, tableNames);

            File file = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file.length())
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}

