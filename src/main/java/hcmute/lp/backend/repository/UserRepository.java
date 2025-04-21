package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    // Tìm người dùng theo vai trò
    List<User> findByRoleIn(List<Role> roles);
    // Đếm số người dùng theo tên vai trò
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = :roleName")
    long countByRoleName(String roleName);
    // Đếm số người dùng đang hoạt động
    long countByActiveTrue();
    // Đếm số người dùng không hoạt động
    long countByActiveFalse();
    // Thống kê số người dùng theo phòng ban
    @Query("SELECT new map(d.name as name, COUNT(u) as count) FROM User u JOIN u.department d GROUP BY d.name")
    Map<String, Long> countByDepartment();
    // Tìm người dùng theo phòng ban
    List<User> findByDepartment(Department department);
    // Tìm người dùng theo trạng thái hoạt động
    List<User> findByActive(boolean isActive);

    @Query("SELECT NEW map(d.name as departmentName, COUNT(u) as count) FROM User u JOIN u.department d GROUP BY d.name")
    List<Map<String, Object>> getDepartmentCounts();
}
