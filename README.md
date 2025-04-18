# Intern Project Backend

## Giới thiệu

Đây là dự án backend cho hệ thống quản lý bán hàng, được xây dựng phục vụ cho mục đích thực tập và học tập. Dự án cung cấp các API phục vụ quản lý người dùng, sản phẩm, danh mục, thương hiệu, nhà cung cấp, đơn hàng, giảm giá, phòng ban, xác thực người dùng, v.v.

## Công nghệ sử dụng

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security (JWT)**
- **Lombok**
- **MapStruct**
- **Swagger/OpenAPI**
- **Hệ quản trị cơ sở dữ liệu**: MySQL
- **Maven**

## Cấu trúc dự án

- `model`: Định nghĩa các entity, DTO, mapper.
- `repository`: Các interface thao tác với database.
- `service`: Xử lý logic nghiệp vụ.
- `controller`: Định nghĩa các REST API.
- `security`: Xác thực, phân quyền.
- `exception`: Xử lý ngoại lệ.
- `config`: Cấu hình ứng dụng.

## Hướng dẫn chạy dự án

1. **Clone source code** về máy:
   ```bash
   git clone <repo-url>
   ```

2. **Cấu hình database** trong file `application.properties` hoặc `application.yml`.

3. **Build và chạy ứng dụng**:
   ```bash
   ./mvnw spring-boot:run
   ```
   hoặc dùng IDE (IntelliJ, Eclipse) để chạy class `BackendApplication.java`.

4. **Truy cập Swagger UI** để thử API:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```
---
