package servlets;

import dao.PostgresUserDAO;
import entities.User;
import services.UserStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("sign_up")
public class SignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = new User();
        user =(User)session.getAttribute("user");
        if (user == null){
            req.getRequestDispatcher("/signup.jsp").forward(req, resp);}
        else{
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String login;
		String password;
		String email;
		String phone;
		String statusDefault;
		User user = new User();
		PostgresUserDAO postgresUserDAO = new PostgresUserDAO();
		try {
			if (request != null) {
				login = (String) request.getAttribute("login");
				password = (String) request.getAttribute("password");
				email = (String) request.getAttribute("email");
				phone = (String) request.getAttribute("phone");
				statusDefault = UserStatus.ACTIVE.name();

                if (!postgresUserDAO.getUserByLogin(login).isPresent()) {

					if (login != null && password != null) {
						user.setLogin(login);
						user.setPassword(password);
						user.setEmail(email == null ? "n@email" : email);
						user.setPhone(phone == null ? "no phone" : phone);
						user.setStatus(statusDefault);
						
						HttpSession registrationSession = request.getSession();
						registrationSession.setAttribute("user", user);
						postgresUserDAO.createNew(user);
						request.getRequestDispatcher("/pricelist.jsp").forward(request, response);

					} else {
						request.getRequestDispatcher("/signup.jsp").forward(request, response);
					}
				} else {
					request.getRequestDispatcher("/signin.jsp").forward(request, response);
				}
			}
		} catch (ServletException serve) {
			serve.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}