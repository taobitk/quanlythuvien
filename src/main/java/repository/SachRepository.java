package repository;

import connect.DatabaseConnection;
import model.Sach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SachRepository {

    public List<Sach> findAll() {
        List<Sach> danhSachSach = new ArrayList<>();
        String sql = "SELECT MaSach, TenSach, TacGia, MoTa, SoLuong FROM Sach";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (conn == null) {
                System.err.println("findAll(): Không thể kết nối đến cơ sở dữ liệu.");
                return danhSachSach;
            }
            while (rs.next()) {
                Sach sach = new Sach(
                        rs.getInt("MaSach"),
                        rs.getString("TenSach"),
                        rs.getString("TacGia"),
                        rs.getString("MoTa"),
                        rs.getInt("SoLuong")
                );
                danhSachSach.add(sach);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn danh sách sách: " + e.getMessage());
            e.printStackTrace();
        }
        return danhSachSach;
    }

    public Sach findById(int maSach) {
        Sach sach = null;
        String sql = "SELECT MaSach, TenSach, TacGia, MoTa, SoLuong FROM Sach WHERE MaSach = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                System.err.println("findById(): Không thể kết nối đến cơ sở dữ liệu.");
                return null;
            }

            pstmt.setInt(1, maSach);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    sach = new Sach(
                            rs.getInt("MaSach"),
                            rs.getString("TenSach"),
                            rs.getString("TacGia"),
                            rs.getString("MoTa"),
                            rs.getInt("SoLuong")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm sách theo ID " + maSach + ": " + e.getMessage());
            e.printStackTrace();
        }
        return sach;
    }

    public boolean updateSoLuong(int maSach, int soLuongThayDoi, Connection conn) throws SQLException {
        String sql = "UPDATE Sach SET SoLuong = SoLuong + ? WHERE MaSach = ? AND (SoLuong + ?) >= 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, soLuongThayDoi);
            pstmt.setInt(2, maSach);
            pstmt.setInt(3, soLuongThayDoi);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}