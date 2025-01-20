package ibrahim.compulynxtest.StudentManagement.Service;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExcelExporter {

    // Method to save generated data to an Excel file

    @Value("${files.dir.upload}")
    private String folderPath;
    public ApiResponse<?> saveStudentDataToExcel(List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook()) {

            // Create a sheet in the Excel workbook
            Sheet sheet = workbook.createSheet("Students");

            // Set up the header row
            String[] headers = {"studentId", "firstName", "lastName", "DOB", "class", "score", "status", "photoPath"};
            int rowNum = 0;
            Row rowHeaders = sheet.createRow(rowNum++);
            createRowHeader(rowHeaders, headers);

            sheet.setColumnWidth(0, 6 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 12 * 256);
            sheet.setColumnWidth(3, 10 * 256);
            sheet.setColumnWidth(4, 6 * 256);
            sheet.setColumnWidth(5, 5 * 256);
            sheet.setColumnWidth(6, 5 * 256);
            sheet.setColumnWidth(7, 40 * 256);

            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getLastName());
                row.createCell(3).setCellValue(student.getDob().toString());
                row.createCell(4).setCellValue(student.getStudentClass().name());
                row.createCell(5).setCellValue(student.getScore());
                row.createCell(6).setCellValue(student.getStatus());
                row.createCell(7).setCellValue(student.getPhotoPath());
            }

            File directory = new File(folderPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Create folder if it doesn't exist
            }

            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");

            String fileName = students.size()+"_Students_"+formater.format(new Date())+".xlsx";
            File file = new File(directory, fileName);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                log.info("Excel file has been written to {}", file.getAbsolutePath());
                return ResponseBuilder.success("Excel file has been written to "+ file.getAbsolutePath(),file.getAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to save student data to Excel file: {}", e.getMessage());
            return ResponseBuilder.error("Failed to save student data to Excel file: "+ e.getMessage(),null);
        }
    }

    private void createRowHeader(Row row, String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    public ByteArrayInputStream export(List<Student> students) {
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {

            // Create a sheet in the Excel workbook
            Sheet sheet = workbook.createSheet("Students");

            // Set up the header row
            String[] headers = {"studentId", "firstName", "lastName", "DOB", "class", "score", "status", "photoPath"};
            int rowNum = 0;
            Row rowHeaders = sheet.createRow(rowNum++);
            createRowHeader(rowHeaders, headers);

            sheet.setColumnWidth(0, 6 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 12 * 256);
            sheet.setColumnWidth(3, 10 * 256);
            sheet.setColumnWidth(4, 6 * 256);
            sheet.setColumnWidth(5, 5 * 256);
            sheet.setColumnWidth(6, 5 * 256);
            sheet.setColumnWidth(7, 40 * 256);

            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getStudentId());
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getLastName());
                row.createCell(3).setCellValue(student.getDob().toString());
                row.createCell(4).setCellValue(student.getStudentClass().name());
                row.createCell(5).setCellValue(student.getScore());
                row.createCell(6).setCellValue(student.getStatus());
                row.createCell(7).setCellValue(student.getPhotoPath());
            }

          workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
} catch (IOException e) {
        log.error("fail to import data to Excel file: {}", e.getMessage());
        throw new RuntimeException("fail to import data to Excel file: {}" + e.getMessage());
        }
        }
}
