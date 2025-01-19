package ibrahim.compulynxtest.StudentManagement.Controller;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.StudentManagement.Service.StudentService;
import ibrahim.compulynxtest.Utils.ApiResponse;
import ibrahim.compulynxtest.Utils.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
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
}
