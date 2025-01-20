package ibrahim.compulynxtest.StudentManagement.Controller;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.StudentManagement.Service.StudentService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
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
        }catch (Exception e){
            ApiResponse<?> response =ResponseBuilder.error(e.getLocalizedMessage(),null);
            return ResponseEntity.ok(response);

        }


    }
    @GetMapping("upload")
    public ResponseEntity<ApiResponse<?>> upload() {
        try {
            ApiResponse res = studentService.upload();
            return ResponseEntity.ok(res);
        }catch (Exception e){
            ApiResponse<?> response =ResponseBuilder.error(e.getLocalizedMessage(),null);
            return ResponseEntity.ok(response);

        }


    }
    @DeleteMapping("delete")
    public ResponseEntity<ApiResponse<?>> deleteUser(@RequestParam Long id) {
        try {
            ApiResponse res = studentService.upload();
            return ResponseEntity.ok(res);
        }catch (Exception e){
            ApiResponse<?> response =ResponseBuilder.error(e.getLocalizedMessage(),null);
            return ResponseEntity.ok(response);

        }


    }
    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable("id") Long id) {
        try {
            ApiResponse res = studentService.findById(id);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            ApiResponse<?> response =ResponseBuilder.error(e.getLocalizedMessage(),null);
            return ResponseEntity.ok(response);

        }


    }

    @PutMapping("update")
    public ResponseEntity<ApiResponse<?>> updateUser(@RequestBody Student student) {
        try {
            ApiResponse res = studentService.updateUser(student);
            return ResponseEntity.ok(res);
        }catch (Exception e){
            ApiResponse<?> response =ResponseBuilder.error(e.getLocalizedMessage(),null);
            return ResponseEntity.ok(response);

        }
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> fetchStudents() {
        try {
            ApiResponse res = studentService.fetchStudents();
            return ResponseEntity.ok(res);
        }catch (Exception e){
            ApiResponse<?> response =ResponseBuilder.error(e.getLocalizedMessage(),null);
            return ResponseEntity.ok(response);

        }


    }
}
