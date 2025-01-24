package ibrahim.compulynxtest.StudentManagement.Controller;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.StudentManagement.Repository.Studentrepo;
import ibrahim.compulynxtest.StudentManagement.Service.ExcelExporter;
import ibrahim.compulynxtest.StudentManagement.Service.StudentService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("api/v1/students")
public class StudentController {

    @Value("${files.dir.photos}")
    private String photosPath;
    private final StudentService studentService;
    private final Studentrepo studentrepo;
    private final ExcelExporter excelExporter;

    public StudentController(StudentService studentService, Studentrepo studentrepo, ExcelExporter excelExporter) {
        this.studentService = studentService;
        this.studentrepo = studentrepo;
        this.excelExporter = excelExporter;
    }


    @GetMapping("generate")
    public ResponseEntity<ApiResponse<?>> generateData(@RequestParam Integer numberofrecords) {

        ApiResponse res = studentService.generateData(numberofrecords);
        return ResponseEntity.ok(res);


    }

    @GetMapping("process")
    public ResponseEntity<ApiResponse<?>> processData() {
        try {
            ApiResponse res = studentService.processData();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ApiResponse<?> response = ResponseBuilder.error(e.getLocalizedMessage(), null);
            return ResponseEntity.ok(response);

        }


    }

    @GetMapping("class")
    public ResponseEntity<ApiResponse<?>> filterByClass(@RequestParam String studentClass) {
        try {
            ApiResponse res = studentService.findByClass(studentClass);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ApiResponse<?> response = ResponseBuilder.error(e.getLocalizedMessage(), null);
            return ResponseEntity.ok(response);

        }


    }

    @GetMapping("upload")
    public ResponseEntity<ApiResponse<?>> upload() {
        try {
            ApiResponse res = studentService.upload();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ApiResponse<?> response = ResponseBuilder.error(e.getLocalizedMessage(), null);
            return ResponseEntity.ok(response);

        }


    }

    @DeleteMapping("delete")
    public ResponseEntity<ApiResponse<?>> deleteUser(@RequestParam Long id) {
        try {
            ApiResponse res = studentService.deleteById(id);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ApiResponse<?> response = ResponseBuilder.error(e.getLocalizedMessage(), null);
            return ResponseEntity.ok(response);

        }


    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable("id") Long id) {
        try {
            ApiResponse res = studentService.findById(id);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ApiResponse<?> response = ResponseBuilder.error(e.getLocalizedMessage(), null);
            return ResponseEntity.ok(response);

        }


    }

    @PutMapping("update")
    public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody Student student) {
        log.info("Updating student with id {}", student.getStudentId());
        try {
            ApiResponse<?> res = studentService.updateUserDraft(student);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ApiResponse<?> response = ResponseBuilder.error(e.getLocalizedMessage(), null);
            return ResponseEntity.ok(response);

        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> fetchStudents() {
        try {
            ApiResponse res = studentService.fetchStudents();
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            ApiResponse<?> response = ResponseBuilder.error(e.getLocalizedMessage(), null);
            return ResponseEntity.ok(response);

        }


    }

    @GetMapping("export")
    public ResponseEntity<Resource> export(@RequestParam String startDate, @RequestParam String endDate) {
        log.info("Exporting student data");
        List<Student> studentsbydobrange = studentrepo.findByByDOBRange(startDate, endDate);

        log.info("Found {} Students", studentsbydobrange.size());
        String filename = "Student_report";
        InputStreamResource file = null;
        file = new InputStreamResource(excelExporter.export(studentsbydobrange));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping("file")
    public ResponseEntity<ApiResponse<String>> handleFileUpload(@RequestParam("file") MultipartFile file) {
        log.info("Uploading file {}", file.getOriginalFilename());
        try {
            File uploadDir = new File(photosPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            File savedFile = new File(uploadDir, file.getOriginalFilename());
            file.transferTo(savedFile);
            String fileUrl = "http://localhost:9200/" + savedFile.getName();

            log.info("File uploaded successfully:" +fileUrl);
            return ResponseEntity.status(200).body(ResponseBuilder.success(fileUrl,null));

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return ResponseEntity.status(500).body(ResponseBuilder.error("File upload failed",null));
        }
    }
}
