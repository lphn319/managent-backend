package hcmute.lp.backend.model.dto.employee;

import hcmute.lp.backend.model.dto.department.DepartmentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDto {
    private Long id;
    private String employeeId;
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private DepartmentDto department;
    private String email; // Thêm từ User
    private String status; // Thêm từ User
}