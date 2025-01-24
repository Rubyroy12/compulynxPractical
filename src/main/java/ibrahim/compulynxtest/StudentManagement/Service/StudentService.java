package ibrahim.compulynxtest.StudentManagement.Service;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.StudentManagement.Models.Updaterequest;
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
    ApiResponse<?> approveUpdate(Updaterequest updaterequest);
    ApiResponse<Student> updateUser(Student student);
    ApiResponse<Student> updateUserDraft(Student student);
    ApiResponse<?> findById(Long studentId);
    ApiResponse<List<Student>> findByClass(String studentClass);
    ApiResponse<List<Student>> findByState(String state);






}
