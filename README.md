# Quản lý sản phẩm (Java Swing + JDBC)

Ứng dụng Desktop quản lý sản phẩm cơ bản (CRUD) sử dụng **Java Swing** làm giao diện và **JDBC** để kết nối trực tiếp với cơ sở dữ liệu.

## Tính năng

- Hiển thị danh sách sản phẩm lên bảng (JTable).
- Thêm sản phẩm mới (gồm Tên, Giá, Số lượng).
- Cập nhật (Sửa) thông tin sản phẩm.
- Xóa sản phẩm khỏi hệ thống.
- Làm mới (Reset) form nhập liệu.

## Công nghệ sử dụng

- Ngôn ngữ: Java (Java Swing cho GUI)
- Cơ sở dữ liệu: MySQL
- Kết nối CSDL: JDBC (MySQL Connector/J 8.3.0)
- IDE: Eclipse / IntelliJ IDEA / NetBeans

## Cấu trúc chính

- `ProductManagerUI.java`: Chứa toàn bộ mã nguồn bao gồm thiết kế giao diện (JFrame, JPanel) và logic xử lý CRUD, kết nối database.
- `shopdb.sql`: File script chứa câu lệnh tạo Database `shopdb`, bảng `products` và dữ liệu mẫu.
- `mysql-connector-j-8.3.0.jar`: Thư viện Driver để Java có thể giao tiếp với MySQL.

## Yêu cầu trước khi chạy

- Java Development Kit (JDK 8 trở lên)
- MySQL Server đang chạy (XAMPP, MySQL Workbench...)
- IDE dành cho Java (khuyến nghị Eclipse)

## Cài đặt và chạy

### 1) Tạo database

Mở trình quản lý MySQL của bạn (ví dụ: MySQL Workbench) và chạy script từ file `shopdb.sql`, hoặc copy/paste đoạn code sau:

```sql
CREATE DATABASE shopdb;
USE shopdb;

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL
);

-- Thêm dữ liệu mẫu (tuỳ chọn)
INSERT INTO products (name, price, quantity) VALUES ('Laptop', 1500.00, 10);
INSERT INTO products (name, price, quantity) VALUES ('Smartphone', 800.00, 20);
INSERT INTO products (name, price, quantity) VALUES ('Tablet', 500.00, 15);
```
### 2) Cấu hình kết nối CSDL

Mở file `ProductManagerUI.java` và tìm đến hàm `connectToDatabase()`. Bạn cần chỉnh sửa thông tin đăng nhập cho khớp với MySQL trên máy của bạn:

* **URL kết nối:** `jdbc:mysql://localhost:3306/shopdb` (giữ nguyên nếu bạn dùng cổng mặc định 3306).
* **Tên đăng nhập:** `String user = "root";` (thường mặc định là `root`).
* **Mật khẩu:** `String password = "password";` (thay chữ `password` bằng mật khẩu MySQL của bạn, nếu không có mật khẩu thì để rỗng `""`).

### 3) Thêm thư viện cần thiết (JDBC Driver)

Để code Java hiểu được MySQL, bạn bắt buộc phải thêm file `mysql-connector-j-8.3.0.jar` vào dự án:

* **Trong Eclipse:** Chuột phải vào Project -> `Build Path` -> `Configure Build Path...` -> Tab `Libraries` -> `Add External JARs...` -> Chọn file `mysql-connector-j-8.3.0.jar` -> `Apply and Close`.

### 4) Chạy ứng dụng

1. Mở file `ProductManagerUI.java` trong IDE.
2. Click chuột phải chọn `Run As` -> `Java Application` (hoặc nhấn nút Run).
3. Cửa sổ giao diện "Quản lý sản phẩm" sẽ hiện lên.

## Cách sử dụng nhanh

* **Thêm mới:** Nhập Tên, Giá, Số lượng vào các ô trống rồi bấm **Thêm**. (Không cần nhập ID vì CSDL tự động tăng).
* **Sửa:** Click chọn 1 sản phẩm trên bảng, sửa thông tin trên các ô nhập liệu rồi bấm **Sửa**.
* **Xóa:** Click chọn 1 sản phẩm trên bảng, bấm **Xóa**.
* **Nhập lại:** Bấm **Nhập lại** để xóa trắng các ô text, chuẩn bị nhập sản phẩm mới.