package ibrahim.compulynxtest.StudentManagement.Service;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.Utils.ApiResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface StudentService {



    ApiResponse<?> generateData(int nofrecords);
    ApiResponse<?> processData() throws IOException;
    ApiResponse<?> upload();
    ApiResponse<List<Student>> fetchStudents();
    ApiResponse<?> deleteById(Long studentId);
    ApiResponse<Student> updateUser(Student student);
    ApiResponse<Student> findById(Long studentId);
    ApiResponse<List<Student>> findByClass(String studentClass);





}
