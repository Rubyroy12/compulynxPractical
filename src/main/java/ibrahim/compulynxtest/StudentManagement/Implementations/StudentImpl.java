package ibrahim.compulynxtest.StudentManagement.Implementations;


import com.opencsv.CSVWriter;
import ibrahim.compulynxtest.StudentManagement.Enums.Class;
import ibrahim.compulynxtest.StudentManagement.Enums.Tracker;
import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.StudentManagement.Models.StudentDraft;
import ibrahim.compulynxtest.StudentManagement.Repository.StudentDraftRepo;
import ibrahim.compulynxtest.StudentManagement.Repository.Studentrepo;
import ibrahim.compulynxtest.StudentManagement.Service.ExcelExporter;
import ibrahim.compulynxtest.StudentManagement.Service.StudentService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.DbConnection;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static ibrahim.compulynxtest.Utils.DbConnection.getDBConnection;

@Service
@Slf4j
public class StudentImpl implements StudentService {

    @Value("${files.dir.upload}")
    private String folderPath;


    private static final Faker faker = new Faker();
    private static final Random random = new Random();
    private final ExcelExporter excelExporter;
    private final Studentrepo studentrepo;
    private final StudentDraftRepo draftRepo;


    public StudentImpl(ExcelExporter excelExporter, Studentrepo studentrepo, StudentDraftRepo draftRepo) {
        this.excelExporter = excelExporter;
        this.studentrepo = studentrepo;
        this.draftRepo = draftRepo;
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
                if (file.getName().toLowerCase().endsWith(".xlsx")) { // Process only .xlsx files
                    convertExcelToCSV(file, folderPath + "/Processed_" + file.getName() + ".csv");
                } else {
                    log.warn("Skipping non-Excel file: {}", file.getName());
                }
            }
        }
        return ResponseBuilder.success("Done", null);
    }

    @Override
    public ApiResponse<?> upload() {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            Objects.requireNonNull(folder.listFiles());
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.getName().toLowerCase().endsWith(".xlsx")) {
                    readExcelAndSaveToDB(file.getAbsolutePath());
                } else {
                    log.warn("Skipping non-Excel file: {}", file.getName());
                }
            }
        }
        return ResponseBuilder.success("Done", null);
    }

    @Override
    public ApiResponse<List<Student>> fetchStudents() {

        List<Student> students = studentrepo.findAll();

        return ResponseBuilder.success(students.size() + " students found", students);
    }

    @Override
    public ApiResponse<?> deleteById(Long studentId) {
        Optional<Student> studentcheck = studentrepo.findById(studentId);
        if (studentcheck.isPresent()) {
            studentcheck.get().setStatus(0);
            studentrepo.save(studentcheck.get());
            return ResponseBuilder.success("Student Deleted successfully", null);
        }
        return ResponseBuilder.error("Student Does not exist", null);
    }

    @Override
    public ApiResponse<Student> updateUser(Student student) {
        Optional<Student> studentcheck = studentrepo.findById(student.getStudentId());
        if (studentcheck.isPresent()) {
            Student s = studentrepo.save(student);
            return ResponseBuilder.success("Student updated successfully", s);
        }
        return ResponseBuilder.error("Student Does not exist", null);
    }

    @Override
    public ApiResponse<Student> updateUserDraft(Student student) {
        log.info("---Student update initiated by {} ----",SecurityContextHolder.getContext().getAuthentication().getName() );

        Optional<Student> studentcheck = studentrepo.findById(student.getStudentId());
        if (studentcheck.isPresent()) {

            StudentDraft draft = new StudentDraft();
            draft.setFirstName(student.getFirstName());
            draft.setLastName(student.getLastName());
            draft.setDob(student.getDob());
            draft.setScore(student.getScore());
            draft.setStudentClass(student.getStudentClass());
            draft.setPhotoPath(student.getPhotoPath());
            draft.setStatus(student.getStatus());
            draft.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());
            draft.setModifiedDate(new Date());

            studentcheck.get().setState(Tracker.PENDING);

            draftRepo.save(draft);
            log.info("Saving student update draft...");
            return ResponseBuilder.success("Student update is pending for approval", student);
        }
        return ResponseBuilder.error("Student Does not exist", null);
    }


    @Override
    public ApiResponse<Student> findById(Long studentId) {
        Optional<Student> studentcheck = studentrepo.findById(studentId);
        if (studentcheck.isPresent()) {
            studentrepo.deleteById(studentId);
            return ResponseBuilder.success("Student Found", studentcheck.get());
        }
        return ResponseBuilder.error("Student Does not exist", null);
    }

    @Override
    public ApiResponse<List<Student>> findByClass(String studentClass) {
        List<Student> students = studentrepo.findByStudentClass(Class.valueOf(studentClass));
        return ResponseBuilder.success(students.size() + " students found", students);
    }


    public List<Student> studentDataGenerator(int nofrecords) {
        List<Student> studentArrays = new ArrayList<>();

        for (int i = 1; i <= nofrecords; i++) {
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

    public void readExcelAndSaveToDB(String filePath) {

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis);
             Connection conn = DbConnection.getDBConnection()) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            boolean firstRow = true;

            String sql = "INSERT INTO student (student_id, first_name, last_name,dob, student_class, score, status, photo_path,state) \n" +
                    "VALUES (null, ?, ?, ?, ?, ?, ?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Skip header row
                if (firstRow) {
                    firstRow = false;
                    continue;
                }


                String col2 = getCellValue(row.getCell(1));
                String col3 = getCellValue(row.getCell(2));
                String col4 = getCellValue(row.getCell(3));
                String col5 = getCellValue(row.getCell(4));
                String col6 = getCellValue(row.getCell(5));
                String col7 = String.valueOf(1);
                String col8 = "";
                String col9 = Tracker.ACTIVE.name();


                pstmt.setString(1, col2);
                pstmt.setString(2, col3);
                pstmt.setString(3, col4);
                pstmt.setString(4, col5);
                pstmt.setString(5, col6 + 5);
                pstmt.setString(6, col7);
                pstmt.setString(7, col8);
                pstmt.setString(8, col9);

                pstmt.addBatch();
            }

            conn.setAutoCommit(false);
            pstmt.executeBatch();
            conn.commit();
            log.info("Data inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void initiateUpdate(Student student) {
//
//
//        studentDraftRepository.save(draft);
//    }
}
