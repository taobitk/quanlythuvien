package servlet;

import model.dto.ThongTinMuonSachDTO;
import repository.TheMuonSachRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/thong-ke-muon-sach")
public class ThongKeServlet extends HttpServlet {

    private TheMuonSachRepository theMuonSachRepository;

    @Override
    public void init() throws ServletException {
        this.theMuonSachRepository = new TheMuonSachRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String tenSachSearch = request.getParameter("tenSachSearch");
        String tenHocSinhSearch = request.getParameter("tenHocSinhSearch");

        List<ThongTinMuonSachDTO> loanList = theMuonSachRepository.findActiveLoansWithDetails(tenSachSearch, tenHocSinhSearch);

        request.setAttribute("loanList", loanList);
        request.setAttribute("tenSachSearch", tenSachSearch);
        request.setAttribute("tenHocSinhSearch", tenHocSinhSearch);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/thong_ke.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}