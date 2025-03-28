package ru.testPr.test.Service;

import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

@Service
public class ExcelReportGenerator {
	private final DataSource dataSource;
	private final Map<String, String> columnTranslations = ExcelHeaderMapper.getColumnTranslations();

    public ExcelReportGenerator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generateReport(String filePath, List<String> tableNames) throws SQLException, IOException {
    	List<String> queries = List.of("SELECT * FROM get_audience_usage()", "SELECT * FROM get_info()",
    								"SELECT * FROM get_audience_info()", "SELECT * FROM get_result_info()");
    	List<String> names = List.of("Аудитория", "Общая информация", "Сводка по аудиториям", "Результаты");
    	
        try (Connection connection = dataSource.getConnection();
             Workbook workbook = new XSSFWorkbook()) {

            for (int k = 0;k<names.size();k++) {
                Sheet sheet = workbook.createSheet(names.get(k));

                try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(queries.get(k))) {

                    Row headerRow = sheet.createRow(0);
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        Cell headerCell = headerRow.createCell(i - 1);
                        String columnName = resultSet.getMetaData().getColumnName(i);
                        headerCell.setCellValue(columnTranslations.getOrDefault(columnName, columnName));
                        headerCell.setCellStyle(createHeaderCellStyle(workbook));
                    }

                    int rowIndex = 1;
                    int startInd = 1;
                    int endInd = 1;
                    int aud = 0;
                    while (resultSet.next()) {
                        Row row = sheet.createRow(rowIndex++);
                        for (int i = 1; i <= columnCount; i++) {
                            Cell cell = row.createCell(i - 1);
                            Object value = resultSet.getObject(i);
                            if (value instanceof Integer) {
	                            if(i == 1 && aud != (Integer) value) {
//	                            	System.out.println(value.toString());
	                            	cell.setCellStyle(createCellStyle(workbook));
	                            	aud = (Integer) value;
	                            	if(endInd - startInd >= 2) {
	                            		sheet.addMergedRegion(new CellRangeAddress(startInd, endInd-1, 0, 0));
	                            		if(k == 3) {
	                            			sheet.addMergedRegion(new CellRangeAddress(startInd, endInd-1, 1, 1));
	                            			sheet.addMergedRegion(new CellRangeAddress(startInd, endInd-1, 2, 2));
	                            			sheet.addMergedRegion(new CellRangeAddress(startInd, endInd-1, 3, 3));
	                            		}
	                            	}
	                            	startInd = endInd;
//	                            	System.out.println(startInd);
//	                            	System.out.println(endInd);
	                            }
                            }

                            if (value instanceof Integer) {
                                cell.setCellValue((Integer) value);
                            } else if (value instanceof Double) {
                                cell.setCellValue((Double) value);
                            } else if (value instanceof Long) {
                                cell.setCellValue((Long) value);
                            } else if (value instanceof Float) {
                                cell.setCellValue((Float) value);
                            } else if (value instanceof Boolean) {
                                cell.setCellValue((Boolean) value ? "Да" : "Нет");
                            }else {
                                cell.setCellValue(value != null ? value.toString() : "");
                            }
                        }
                        endInd++;
                    }
//                    System.out.println(startInd);
//                	System.out.println(endInd);
                    if(endInd - startInd >= 2) {
                		sheet.addMergedRegion(new CellRangeAddress(startInd, endInd-1, 0, 0));
                	}

                    for (int i = 0; i < columnCount; i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    
    
    private CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
}

