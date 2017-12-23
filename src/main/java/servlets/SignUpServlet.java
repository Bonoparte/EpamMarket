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
import java.util.Optional;

@WebServlet(name = "Registrarion", value = "/sign_up")
public class SignUpServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws
			ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			request.getRequestDispatcher("/signup.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
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
		Optional<User> optionalUser;
		PostgresUserDAO postgresUserDAO = new PostgresUserDAO();
		try {
			if (request != null) {
				login =  request.getParameter("login");
				password = request.getParameter("password");
				email = request.getParameter("email");
				phone = request.getParameter("phone");
				statusDefault = UserStatus.ACTIVE.name();

				if (!postgresUserDAO.getUserByLogin(login).isPresent()) {

					if (login != null && password != null) {
						user.setLogin(login);
						user.setPassword(password);
						user.setEmail(email == null ? "n@email" : email);
						user.setPhone(phone == null ? "no phone" : phone);
						user.setStatus(statusDefault);

						HttpSession registrationSession = request.getSession();

						postgresUserDAO.createNew(user);
						optionalUser = postgresUserDAO.getUserByLogin(login);
						if (optionalUser.isPresent()) {
							registrationSession.setAttribute("user", user);
						} else {
							request.getRequestDispatcher("/signup.jsp").forward(request,response);
						}
						response.sendRedirect("/price_list");
					} else {
						request.getRequestDispatcher("/signup.jsp").forward(request, response);
					}
				} else {
					request.getRequestDispatcher("/signup.jsp").forward(request, response);
					request.setAttribute("tag",login);
				}
			}
		} catch (ServletException serve) {
			serve.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}