package ibrahim.compulynxtest.StudentManagement.Implementations;


import ibrahim.compulynxtest.StudentManagement.Enums.Class;
import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.StudentManagement.Service.ExcelExporter;
import ibrahim.compulynxtest.StudentManagement.Service.StudentService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class StudentImpl implements StudentService {


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
        return ResponseBuilder.error(response.getMessage(), null );


    }
//data processing
    @Override
    public ApiResponse<?> processData() {
        return null;
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
}
