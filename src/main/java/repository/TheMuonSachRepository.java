package repository;

import model.TheMuonSach;
import model.dto.ThongTinMuonSachDTO;
import connect.DatabaseConnection; // Đảm bảo import đúng lớp Connection

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TheMuonSachRepository {

    // Phương thức save từ các yêu cầu trước
    public boolean save(TheMuonSach theMuonSach, Connection conn) throws SQLException {
        String sql = "INSERT INTO TheMuonSach (MaSach, MaHocSinh, TrangThai, NgayMuon, NgayTra) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, theMuonSach.getMaSach());
            pstmt.setInt(2, theMuonSach.getMaHocSinh());
            pstmt.setBoolean(3, theMuonSach.isTrangThai());
            pstmt.setDate(4, theMuonSach.getNgayMuon());
            if (theMuonSach.getNgayTra() != null) {
                pstmt.setDate(5, theMuonSach.getNgayTra());
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    // Phương thức mới cho Yêu cầu 3
    public List<ThongTinMuonSachDTO> findActiveLoansWithDetails(String searchTenSach, String searchTenHocSinh) {
        List<ThongTinMuonSachDTO> results = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT tms.MaMuonSach, tms.MaSach, s.TenSach, s.TacGia, hs.HoTen, hs.Lop, tms.NgayMuon, tms.NgayTra " +
                        "FROM TheMuonSach tms " +
                        "JOIN Sach s ON tms.MaSach = s.MaSach " +
                        "JOIN HocSinh hs ON tms.MaHocSinh = hs.MaHocSinh " +
                        "WHERE tms.TrangThai = TRUE "
        );

        List<Object> params = new ArrayList<>();

        if (searchTenSach != null && !searchTenSach.trim().isEmpty()) {
            sqlBuilder.append("AND s.TenSach LIKE ? ");
            params.add("%" + searchTenSach.trim() + "%");
        }
        if (searchTenHocSinh != null && !searchTenHocSinh.trim().isEmpty()) {
            sqlBuilder.append("AND hs.HoTen LIKE ? ");
            params.add("%" + searchTenHocSinh.trim() + "%");
        }

        sqlBuilder.append("ORDER BY tms.NgayMuon DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            if (conn == null) {
                System.err.println("findActiveLoansWithDetails(): Không thể kết nối CSDL.");
                return results;
            }
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ThongTinMuonSachDTO dto = new ThongTinMuonSachDTO(
                            rs.getInt("MaMuonSach"),
                            rs.getInt("MaSach"),
                            rs.getString("TenSach"),
                            rs.getString("TacGia"),
                            rs.getString("HoTen"),
                            rs.getString("Lop"),
                            rs.getDate("NgayMuon"),
                            rs.getDate("NgayTra")
                    );
                    results.add(dto);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn thống kê sách đang mượn: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    // Phương thức mới cho Yêu cầu 3 (dùng trong SP thay thế)
    // Bạn có thể giữ lại hoặc xóa nếu chắc chắn không dùng ở đâu khác
    public boolean markAsReturned(int maMuonSach, Connection conn) throws SQLException {
        String sql = "UPDATE TheMuonSach SET TrangThai = FALSE WHERE MaMuonSach = ? AND TrangThai = TRUE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maMuonSach);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected == 1;
        }
    }
}