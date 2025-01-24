package ibrahim.compulynxtest.StudentManagement.Models;


import lombok.Data;

@Data
public class Updaterequest {


    private Long studentId;
    private String status;
    private String reason;
}
