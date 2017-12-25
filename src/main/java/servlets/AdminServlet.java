package servlets;

import entities.User;
import lombok.extern.log4j.Log4j2;
import services.AdminService;
import services.ChangeStatusService;
import services.UserStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Log4j2
@WebServlet(name = "Admin", value = "/adminpage")
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<User> userList = AdminService.getInstance().getUserList();
        HttpSession session = req.getSession();
        UserStatus status;
        User user = (User) session.getAttribute("user");
        if (user == null) {
            log.info("User null detected, redirect to start page.");
            resp.sendRedirect("/");
        } else {
            status = UserStatus.valueOf(user.getStatus());
            switch (status) {
                case ACTIVE:
                    log.info("User " + user.toString() + "\ndetected, redirect to start page.");
                    resp.sendRedirect("/");
                    break;
                case ADMIN:
                    try {
                        log.info("User " + user.toString() + "\ndetected, redirect to admin page.");
                        req.setAttribute("users", userList);
                        req.getRequestDispatcher("/adminpage.jsp").forward(req, resp);
                    } catch (RuntimeException e) {
                        log.debug("Servlet dropped down because of \n" + e.getMessage());
                    }
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Integer.valueOf(req.getParameter("userId"));
        log.info("User going to adminpage.");
        ChangeStatusService.getInstance().changeStatusById(id);
        resp.sendRedirect("/adminpage");
    }
}
