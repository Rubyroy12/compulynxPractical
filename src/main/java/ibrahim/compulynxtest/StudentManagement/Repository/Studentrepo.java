package ibrahim.compulynxtest.StudentManagement.Repository;

import ibrahim.compulynxtest.StudentManagement.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Studentrepo extends JpaRepository<Student,Long> {
}
