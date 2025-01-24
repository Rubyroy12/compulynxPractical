package ibrahim.compulynxtest.StudentManagement.Repository;

import ibrahim.compulynxtest.StudentManagement.Models.StudentDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDraftRepo extends JpaRepository<StudentDraft,Long> {
}
