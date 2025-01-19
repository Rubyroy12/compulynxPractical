package ibrahim.compulynxtest.StudentManagement.Implementations;


import com.opencsv.CSVWriter;
import ibrahim.compulynxtest.StudentManagement.Enums.Class;
import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.StudentManagement.Service.ExcelExporter;
import ibrahim.compulynxtest.StudentManagement.Service.StudentService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class StudentImpl implements StudentService {

    @Value("${files.dir.upload}")
    private String folderPath;

    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private final ExcelExporter excelExporter;

    public StudentImpl(ExcelExporter excelExporter) {
        this.excelExporter = excelExporter;
    }


    //data generation
    @Override
    public ApiResponse<?> generateData(int nofrecords) {
        List<Student> students = studentDataGenerator(nofrecords);
        log.info("{} students generated", students.size());
        ApiResponse<?> response = excelExporter.saveStudentDataToExcel(students);
        if ("SUCCESS".equals(response.getStatus()))
            return ResponseBuilder.success(nofrecords + " students generated", response.getMessage());
        return ResponseBuilder.error(response.getMessage(), null);


    }

    //data processing
    @Override
    public ApiResponse<?> processData() throws IOException {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            Objects.requireNonNull(folder.listFiles());
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                convertExcelToCSV(file, folderPath + "Processed_" + file.getName()+".csv");
            }
        }
        return ResponseBuilder.success("Done", null);
    }


    public List<Student> studentDataGenerator(int nofrecords) {
        List<Student> studentArrays = new ArrayList<>();

        for (int i = 1; i < nofrecords; i++) {
            studentArrays.add(generateStudent((long) i));
        }

        return studentArrays;
    }

    public static Student generateStudent(Long id) {
        Student student = new Student();

        student.setStudentId(id);
        student.setFirstName(generateRandomName());
        student.setLastName(generateLastRandomName());
        student.setDob(generateRandomDOB());
        student.setStudentClass(Class.valueOf(generateRandomClass()));
        student.setScore(generateRandomScore());
        student.setStatus(generateRandomStatus());
        student.setPhotoPath(generateRandomPhotoURL());

        return student;
    }

    private static String generateRandomName() {
        return faker.name().firstName();
    }

    private static String generateLastRandomName() {
        return faker.name().lastName();
    }

    private static LocalDate generateRandomDOB() {
        return LocalDate.of(2000, 1, 1)
                .plusDays(random.nextInt((int) ChronoUnit.DAYS.between(LocalDate.of(2000, 1, 1), LocalDate.of(2010, 12, 31))));
    }

    private static String generateRandomClass() {
        String[] classes = getClassNames();
        return classes[random.nextInt(classes.length)];
    }

    private static String[] getClassNames() {
        return Arrays.stream(Class.values())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    private static int generateRandomScore() {
        return random.nextInt(31) + 55; // Generates integer between 55 and 85  as in (0-30+55)
    }

    private static int generateRandomStatus() {
        return random.nextInt(2); // 0 or 1
    }

    private static String generateRandomPhotoURL() {
        return "https://randomuser.me/api/portraits/men/" + random.nextInt(1000000) + ".jpg";
    }


    public void convertExcelToCSV(File excelFilePath, String csvFilePath) throws IOException {
        log.info("EXCEL file: {}", excelFilePath);
        log.info("CSV file: {}", csvFilePath);
        try (FileInputStream excelFile = new FileInputStream(excelFilePath
        );
             Workbook workbook = new XSSFWorkbook(excelFile);
             CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath))) {

            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet
            List<String[]> csvData = new ArrayList<>();


            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip the first row

                List<String> rowData = new ArrayList<>();

                for (Cell cell : row) {
                    if (cell.getColumnIndex() == 5) {
                        int originalScore = (int) cell.getNumericCellValue();
                        int modifiedScore = modifyScore(originalScore);
                        rowData.add(String.valueOf(modifiedScore));
                    } else {
                        rowData.add(getCellValue(cell));
                    }
                }

                csvData.add(rowData.toArray(new String[0]));
            }
            csvWriter.writeAll(csvData);
            log.info("Done");
        }
    }

    private int modifyScore(int originalScore) {
        return originalScore + 10; // Example modification: Increase score by 5
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue()); // Convert to int if numeric
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
