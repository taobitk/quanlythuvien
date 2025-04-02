package repository;

import connect.DatabaseConnection;
import model.HocSinh;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HocSinhRepository {

    public List<HocSinh> findAll() {
        List<HocSinh> danhSachHocSinh = new ArrayList<>();
        String sql = "SELECT MaHocSinh, HoTen, Lop FROM HocSinh ORDER BY HoTen";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) {
                System.err.println("HocSinhRepo.findAll(): Không thể kết nối đến cơ sở dữ liệu.");
                return danhSachHocSinh;
            }

            while (rs.next()) {
                HocSinh hs = new HocSinh(
                        rs.getInt("MaHocSinh"),
                        rs.getString("HoTen"),
                        rs.getString("Lop")
                );
                danhSachHocSinh.add(hs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn danh sách học sinh: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachHocSinh;
    }

    public HocSinh findById(int maHocSinh) {
        HocSinh hs = null;
        String sql = "SELECT MaHocSinh, HoTen, Lop FROM HocSinh WHERE MaHocSinh = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("HocSinhRepo.findById(): Không thể kết nối đến cơ sở dữ liệu.");
                return null;
            }
            pstmt.setInt(1, maHocSinh);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hs = new HocSinh(
                            rs.getInt("MaHocSinh"),
                            rs.getString("HoTen"),
                            rs.getString("Lop")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm học sinh theo ID " + maHocSinh + ": " + e.getMessage());
            e.printStackTrace();
        }
        return hs;
    }
}