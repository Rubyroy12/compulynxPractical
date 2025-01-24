package ibrahim.compulynxtest.StudentManagement.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedResponse {


    private List<Student> studentList;
    private double average;
    private int totalScore;
}
