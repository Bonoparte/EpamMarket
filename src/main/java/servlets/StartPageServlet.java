package servlets;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebServlet(name = "StartPage", value = "/")
public class StartPageServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        log.info("\nCUSTOM-INFO-IN-ThreadID = \n" + Thread.currentThread().getId() + ""
                 + " \n"
                 + "and ThreadName = " + Thread.currentThread().getName()
                 + "\nmessage is\ndoPost() method redirect request to doGet() method.");
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            log.info("\nCUSTOM-INFO-IN-ThreadID = \n" + Thread.currentThread().getId() + ""
                     + " \n"
                     + "and ThreadName = " + Thread.currentThread().getName()
                     + "\nmessage is\nUser redirected to index.jsp page. ++ "
                     + getClass().getName());
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            log.debug("\nCUSTOM-INFO-IN-ThreadID = \n" + Thread.currentThread().getId() + ""
                      + " \n"
                      + "and ThreadName = " + Thread.currentThread().getName()
                      + "\nmessage is\nDropped down at " + this.getClass() + " because of \n"
                      + getClass().getName() + " - " + e.getMessage());
        }
    }
}
