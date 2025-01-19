package ibrahim.compulynxtest.StudentManagement.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import ibrahim.compulynxtest.StudentManagement.Enums.Class;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

}


//studentId – numeric incremental by one, starting value 1.
//firstName – string (random alphabet characters)  -3, Max 8
//lastName – string (random alphabet characters) Min -3, Max 8
//DOB – date (random date of birth between 1-1-2000 and 31-12-2010)
//class – string (random class name OPTIONS [Class1, Class2, Class3, Class4, Class5])
//score - numeric (random number between 55 and 85).
//status – numeric (0 – inactive, 1 – active) default 1- active
//photoPath – string (Default – empty string)