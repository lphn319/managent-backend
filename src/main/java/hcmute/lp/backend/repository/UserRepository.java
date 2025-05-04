package hcmute.lp.backend.repository;

import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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

    // Tìm người dùng với phân trang và lọc
    Page<User> findByRoleIn(List<Role> roles, Pageable pageable);

    // Tìm kiếm người dùng theo tên, email, số điện thoại
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phoneNumber LIKE CONCAT('%', :keyword, '%')")
    Page<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // Tìm kiếm kết hợp theo từ khóa và vai trò
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phoneNumber LIKE CONCAT('%', :keyword, '%')) AND " +
            "u.role IN :roles")
    Page<User> findByKeywordAndRoleIn(@Param("keyword") String keyword, @Param("roles") List<Role> roles, Pageable pageable);

    // Tìm kiếm kết hợp theo từ khóa và trạng thái
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phoneNumber LIKE CONCAT('%', :keyword, '%')) AND " +
            "u.status = :status")
    Page<User> findByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") User.UserStatus status, Pageable pageable);

    // Tìm kiếm kết hợp theo từ khóa, vai trò và trạng thái
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phoneNumber LIKE CONCAT('%', :keyword, '%')) AND " +
            "u.role IN :roles AND u.status = :status")
    Page<User> findByKeywordAndRoleInAndStatus(
            @Param("keyword") String keyword,
            @Param("roles") List<Role> roles,
            @Param("status") User.UserStatus status,
            Pageable pageable);

    // Tìm kiếm kết hợp theo từ khóa, vai trò, phòng ban và trạng thái
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "u.phoneNumber LIKE CONCAT('%', :keyword, '%')) AND " +
            "u.role IN :roles AND u.department = :department AND u.status = :status")
    Page<User> findByKeywordAndRoleInAndDepartmentAndStatus(
            @Param("keyword") String keyword,
            @Param("roles") List<Role> roles,
            @Param("department") Department department,
            @Param("status") User.UserStatus status,
            Pageable pageable);

    // Đếm số người dùng theo tên vai trò
    @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = :roleName")
    long countByRoleName(String roleName);

    // Đếm số người dùng theo trạng thái
    long countByStatus(User.UserStatus status);

    // Thống kê số người dùng theo phòng ban
    @Query("SELECT NEW map(d.name as departmentName, COUNT(u) as count) FROM User u JOIN u.department d GROUP BY d.name")
    List<Map<String, Object>> getDepartmentCounts();

    // Tìm người dùng theo phòng ban
    List<User> findByDepartment(Department department);

    // Tìm người dùng theo phòng ban phân trang
    Page<User> findByDepartment(Department department, Pageable pageable);

    // Tìm người dùng theo trạng thái hoạt động
    List<User> findByStatus(User.UserStatus status);

    // Tìm người dùng theo trạng thái hoạt động phân trang
    Page<User> findByStatus(User.UserStatus status, Pageable pageable);

    // Tìm người dùng theo tên vai trò
    @Query("SELECT u FROM User u WHERE u.role.name IN :names")
    Page<User> findByRoleNameIn(@Param("names") Collection<String> names, Pageable pageable);
}