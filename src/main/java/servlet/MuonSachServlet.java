package servlet;

import connect.DatabaseConnection;
import model.HocSinh;
import model.Sach;
import model.TheMuonSach;
import repository.HocSinhRepository;
import repository.SachRepository;
import repository.TheMuonSachRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/muon-sach")
public class MuonSachServlet extends HttpServlet {

    private SachRepository sachRepository;
    private HocSinhRepository hocSinhRepository;
    private TheMuonSachRepository theMuonSachRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void init() throws ServletException {
        this.sachRepository = new SachRepository();
        this.hocSinhRepository = new HocSinhRepository();
        this.theMuonSachRepository = new TheMuonSachRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String maSachStr = request.getParameter("maSach");
        int maSach = 0;
        String generalError = null;
        Sach sach = null;
        List<HocSinh> hocSinhList = new ArrayList<>();

        try {
            maSach = Integer.parseInt(maSachStr);
            sach = sachRepository.findById(maSach);

            if (sach == null) {
                generalError = "Không tìm thấy sách với mã cung cấp.";
            } else if (sach.getSoLuong() <= 0) {
                String errorMessage = "Sách \"" + sach.getTenSach() + "\" đã hết hàng, không thể mượn.";
                response.sendRedirect(request.getContextPath()
                        + "/danh-sach-sach?errorHetSach=true&tenSach="
                        + java.net.URLEncoder.encode(sach.getTenSach(), "UTF-8"));
                return;
            } else {
                hocSinhList = hocSinhRepository.findAll();
                if (hocSinhList.isEmpty()) {
                    generalError = "Không có dữ liệu học sinh để chọn.";
                }
            }
        } catch (NumberFormatException e) {
            generalError = "Mã sách không hợp lệ.";
        } catch (Exception e) {
            generalError = "Đã xảy ra lỗi khi tải dữ liệu: " + e.getMessage();
            e.printStackTrace();
        }

        if (generalError != null) {
            response.sendRedirect(request.getContextPath() + "/danh-sach-sach?error=" + java.net.URLEncoder.encode(generalError, "UTF-8"));
        } else {
            request.setAttribute("sach", sach);
            request.setAttribute("hocSinhList", hocSinhList);
            request.setAttribute("ngayMuon", LocalDate.now().format(DATE_FORMATTER));
            request.setAttribute("minNgayTra", LocalDate.now().toString());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/muon_sach_form.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String maSachStr = request.getParameter("maSach");
        String maHocSinhStr = request.getParameter("maHocSinh");
        String ngayTraStr = request.getParameter("ngayTraInput");

        Map<String, String> errors = new HashMap<>();
        Map<String, String> userInput = new HashMap<>();
        userInput.put("maSach", maSachStr);
        userInput.put("maHocSinh", maHocSinhStr);
        userInput.put("ngayTraInput", ngayTraStr);

        int maSach = -1;
        int maHocSinh = -1;
        LocalDate ngayMuon = LocalDate.now();
        LocalDate ngayTra = null;
        Sach sach = null;

        try {
            maSach = Integer.parseInt(maSachStr);
            sach = sachRepository.findById(maSach);
            if (sach == null) {
                errors.put("form", "Sách không tồn tại.");
            } else if (sach.getSoLuong() <= 0) {
                errors.put("form", "Sách đã hết, không thể mượn vào lúc này.");
            }
        } catch (NumberFormatException e) {
            errors.put("form", "Mã sách không hợp lệ.");
        }

        try {
            maHocSinh = Integer.parseInt(maHocSinhStr);
            if (hocSinhRepository.findById(maHocSinh) == null) {
                errors.put("maHocSinh", "Học sinh được chọn không hợp lệ.");
            }
        } catch (NumberFormatException e) {
            errors.put("maHocSinh", "Vui lòng chọn một học sinh.");
        }

        if (ngayTraStr == null || ngayTraStr.trim().isEmpty()) {
            errors.put("ngayTraInput", "Ngày trả không được để trống.");
        } else {
            try {
                ngayTra = LocalDate.parse(ngayTraStr, DateTimeFormatter.ISO_LOCAL_DATE);
                if (ngayTra.isBefore(ngayMuon)) {
                    errors.put("ngayTraInput", "Ngày trả không được trước ngày mượn (" + ngayMuon.format(DATE_FORMATTER) + ").");
                }
            } catch (DateTimeParseException e) {
                errors.put("ngayTraInput", "Định dạng ngày trả không hợp lệ (cần yyyy-MM-dd).");
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("userInput", userInput);
            if(sach != null) request.setAttribute("sach", sach);
            request.setAttribute("hocSinhList", hocSinhRepository.findAll());
            request.setAttribute("ngayMuon", ngayMuon.format(DATE_FORMATTER));
            request.setAttribute("minNgayTra", LocalDate.now().toString());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/muon_sach_form.jsp");
            dispatcher.forward(request, response);
        } else {
            Connection conn = null;
            boolean success = false;
            try {
                conn = DatabaseConnection.getConnection();
                if (conn == null) {
                    throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
                }
                conn.setAutoCommit(false);

                TheMuonSach tms = new TheMuonSach();
                tms.setMaSach(maSach);
                tms.setMaHocSinh(maHocSinh);
                tms.setTrangThai(true);
                tms.setNgayMuon(Date.valueOf(ngayMuon));
                tms.setNgayTra(Date.valueOf(ngayTra));

                boolean saveLoanOk = theMuonSachRepository.save(tms, conn);
                boolean decreaseQtyOk = false;
                if (saveLoanOk) {
                    decreaseQtyOk = sachRepository.updateSoLuong(maSach, -1, conn);
                }

                if (saveLoanOk && decreaseQtyOk) {
                    conn.commit();
                    success = true;
                    System.out.println("Mượn sách thành công!");
                } else {
                    System.err.println("Gặp lỗi trong quá trình mượn sách, rollback...");
                    if(!saveLoanOk) System.err.println("Lỗi khi lưu thẻ mượn.");
                    if(!decreaseQtyOk) System.err.println("Lỗi khi giảm số lượng sách (có thể sách đã bị mượn hết).");
                    conn.rollback();
                    errors.put("form", "Không thể hoàn tất việc mượn sách do lỗi hệ thống hoặc sách vừa hết.");
                }

            } catch (SQLException e) {
                System.err.println("Lỗi SQL trong quá trình mượn sách, rollback...");
                e.printStackTrace();
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        System.err.println("Lỗi khi rollback: " + ex.getMessage());
                    }
                }
                errors.put("form", "Lỗi cơ sở dữ liệu: " + e.getMessage());

            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException e) {
                        System.err.println("Lỗi khi đóng kết nối: " + e.getMessage());
                    }
                }
            }

            if (success) {
                response.sendRedirect(request.getContextPath() + "/danh-sach-sach?success=true");
            } else {
                request.setAttribute("errors", errors);
                request.setAttribute("userInput", userInput);
                if(sach != null) request.setAttribute("sach", sach);
                request.setAttribute("hocSinhList", hocSinhRepository.findAll());
                request.setAttribute("ngayMuon", ngayMuon.format(DATE_FORMATTER));
                request.setAttribute("minNgayTra", LocalDate.now().toString());
                RequestDispatcher dispatcher = request.getRequestDispatcher("/views/muon_sach_form.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}