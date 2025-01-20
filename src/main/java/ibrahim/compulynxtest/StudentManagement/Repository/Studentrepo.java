package ibrahim.compulynxtest.StudentManagement.Repository;

import ibrahim.compulynxtest.StudentManagement.Enums.Class;
import ibrahim.compulynxtest.StudentManagement.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Studentrepo extends JpaRepository<Student,Long> {

    List<Student> findByStudentClass(Class studentClass);



    @Query(nativeQuery = true,value = "select * FROM student WHERE dob BETWEEN :startDate AND :endDate")
    List<Student> findByByDOBRange(String startDate,String endDate);
}
