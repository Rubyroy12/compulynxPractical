package ibrahim.compulynxtest.StudentManagement.Service;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import ibrahim.compulynxtest.Utils.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {



    ApiResponse<?> generateData(int nofrecords);
    ApiResponse<?> processData();




}