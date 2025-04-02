package servlet;

import model.Sach;
import repository.SachRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/danh-sach-sach")
public class SachListServlet extends HttpServlet {

    private SachRepository sachRepository;

    @Override
    public void init() throws ServletException {
        super.init();
        this.sachRepository = new SachRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Sach> danhSachSach = sachRepository.findAll();
        request.setAttribute("sachList", danhSachSach);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/sachs.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}