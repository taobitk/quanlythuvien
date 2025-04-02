package servlet;

import connect.DatabaseConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

@WebServlet("/tra-sach")
public class TraSachServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String maMuonSachStr = request.getParameter("maMuonSach");
        String maSachStr = request.getParameter("maSach");

        String redirectURL = request.getContextPath() + "/thong-ke-muon-sach";
        String errorMessage = null;
        boolean success = false;

        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            int maMuonSach = Integer.parseInt(maMuonSachStr);
            int maSach = Integer.parseInt(maSachStr);

            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Không thể kết nối cơ sở dữ liệu.");
            }

            String sql = "{call sp_TraSach(?, ?, ?)}";
            cstmt = conn.prepareCall(sql);

            cstmt.setInt(1, maMuonSach);
            cstmt.setInt(2, maSach);
            cstmt.registerOutParameter(3, Types.TINYINT);

            cstmt.execute();

            int procedureResult = cstmt.getInt(3);

            if (procedureResult == 1) {
                success = true;
                System.out.println("Stored procedure sp_TraSach thực thi thành công cho Mã mượn: " + maMuonSach);
            } else {
                errorMessage = "Không thể trả sách. Lượt mượn không hợp lệ hoặc đã được trả.";
                System.err.println("Stored procedure sp_TraSach báo thất bại cho Mã mượn: " + maMuonSach);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi SQL khi gọi Stored Procedure sp_TraSach: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "Lỗi cơ sở dữ liệu khi thực hiện trả sách.";
        } catch (NumberFormatException e) {
            errorMessage = "Mã mượn sách hoặc mã sách không hợp lệ.";
        } catch (Exception e) {
            System.err.println("Lỗi không mong muốn khi trả sách: " + e.getMessage());
            e.printStackTrace();
            errorMessage = "Lỗi hệ thống không mong muốn.";
        } finally {
            if (cstmt != null) {
                try { cstmt.close(); } catch (SQLException e) { /* Bỏ qua */ }
            }
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* Bỏ qua */ }
            }
        }

        if (success) {
            response.sendRedirect(redirectURL + "?returnSuccess=true");
        } else {
            if (errorMessage == null) errorMessage = "Lỗi không xác định khi trả sách.";
            response.sendRedirect(redirectURL + "?returnError=" + java.net.URLEncoder.encode(errorMessage, "UTF-8"));
        }
    }
}