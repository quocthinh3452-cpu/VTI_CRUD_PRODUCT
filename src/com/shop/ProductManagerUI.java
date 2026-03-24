package com.shop;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ProductManagerUI {

    // Cấu hình Database (Dành cho WAMP)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/shopdb";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        // 1. Tạo khung cửa sổ chính
        JFrame frame = new JFrame("Quản Lý Sản Phẩm - DB WAMP");
        frame.setSize(450, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // 2. Tạo các nút bấm
        JButton btnAdd = new JButton("Thêm Sản Phẩm"); // Đã đổi tên nút
        JButton btnView = new JButton("Xem Danh Sách (Console)");
        JButton btnUpdate = new JButton("Sửa Sản Phẩm");
        JButton btnDelete = new JButton("Xóa Sản Phẩm");

        // 3. Sự kiện nút THÊM (Nhập từ bàn phím)
        btnAdd.addActionListener(e -> {
            // Bước 1: Nhập tên
            String proName = JOptionPane.showInputDialog(frame, "Nhập Tên sản phẩm mới:");
            if (proName != null && !proName.trim().isEmpty()) {
                
                // Bước 2: Nhập Order
                String orderStr = JOptionPane.showInputDialog(frame, "Nhập số Order (Vị trí/Thứ tự):");
                if (orderStr != null && !orderStr.trim().isEmpty()) {
                    try {
                        int order = Integer.parseInt(orderStr);
                        
                        // Bước 3: Hỏi trạng thái Hoạt động (Yes/No)
                        int activeChoice = JOptionPane.showConfirmDialog(frame, 
                                "Sản phẩm này có đang hoạt động không?", 
                                "Trạng thái", 
                                JOptionPane.YES_NO_OPTION);
                        boolean isActive = (activeChoice == JOptionPane.YES_OPTION);

                        // Bước 4: Lưu vào Database
                        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                            insertProduct(conn, proName, order, isActive);
                            JOptionPane.showMessageDialog(frame, "Đã thêm thành công sản phẩm:\n" + proName);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(frame, "Lỗi Database: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                        
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Lỗi: Order phải là một số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 4. Sự kiện nút XEM
        btnView.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                getAllProducts(conn);
                JOptionPane.showMessageDialog(frame, "Đã in danh sách ra màn hình Console của Eclipse!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Lỗi kết nối: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 5. Sự kiện nút SỬA (Yêu cầu nhập ID và Tên mới)
        btnUpdate.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(frame, "Nhập ID sản phẩm cần SỬA:");
            if (idStr != null && !idStr.trim().isEmpty()) {
                String newName = JOptionPane.showInputDialog(frame, "Nhập Tên mới cho sản phẩm:");
                if (newName != null && !newName.trim().isEmpty()) {
                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                        int id = Integer.parseInt(idStr);
                        // Tạm thời fix cứng Order = 1 và isActive = true cho Sửa
                        int rows = updateProduct(conn, id, newName, 1, true); 
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(frame, "Đã cập nhật thành công ID " + id);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Không tìm thấy sản phẩm có ID " + id, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Lỗi: ID phải là một số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame, "Lỗi Database: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 6. Sự kiện nút XÓA (Yêu cầu nhập ID)
        btnDelete.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(frame, "Nhập ID sản phẩm cần XÓA:");
            if (idStr != null && !idStr.trim().isEmpty()) {
                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                    int id = Integer.parseInt(idStr);
                    int rows = deleteProduct(conn, id);
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Đã xóa thành công ID " + id);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Không tìm thấy sản phẩm có ID " + id, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Lỗi: ID phải là một số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Lỗi Database: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 7. Gắn nút lên cửa sổ và hiển thị
        frame.add(btnAdd);
        frame.add(btnView);
        frame.add(btnUpdate);
        frame.add(btnDelete);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    // --- CÁC HÀM TƯƠNG TÁC DATABASE (CRUD) ---

    private static void insertProduct(Connection conn, String proName, int order, boolean isActive) throws SQLException {
        String sql = "INSERT INTO Product (proName, `order`, isActive) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, proName);
            pstmt.setInt(2, order);
            pstmt.setBoolean(3, isActive);
            pstmt.executeUpdate();
        }
    }

    private static void getAllProducts(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- DANH SÁCH TỪ NÚT BẤM ---");
            while (rs.next()) {
                int id = rs.getInt("productID");
                String name = rs.getString("proName");
                int order = rs.getInt("order");
                boolean isActive = rs.getBoolean("isActive");
                System.out.println("ID: " + id + " | Tên: " + name + " | Order: " + order + " | Trạng thái: " + (isActive ? "Hoạt động" : "Khóa"));
            }
        }
    }

    private static int updateProduct(Connection conn, int id, String newName, int newOrder, boolean newIsActive) throws SQLException {
        String sql = "UPDATE Product SET proName = ?, `order` = ?, isActive = ? WHERE productID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newOrder);
            pstmt.setBoolean(3, newIsActive);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate();
        }
    }

    private static int deleteProduct(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM Product WHERE productID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        }
    }
}