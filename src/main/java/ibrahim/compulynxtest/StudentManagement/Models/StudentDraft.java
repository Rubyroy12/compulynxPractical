package ibrahim.compulynxtest.StudentManagement.Models;

import ibrahim.compulynxtest.StudentManagement.Enums.Class;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "draft")
public class StudentDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long studentId;
    @Size(min = 3,max = 8)
    private String  firstName;
    @Size(min = 3,max = 8)
    private String  lastName;

    @NotNull(message = "Date of Birth is required")
    @Column(nullable = false)
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Class studentClass;
    private int score;
    private int status=1;
    private String photoPath="";

    private String modifiedBy;
    private Date modifiedDate;

}
