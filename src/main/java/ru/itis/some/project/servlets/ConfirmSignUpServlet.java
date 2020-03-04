package ru.itis.some.project.servlets;

import org.springframework.context.ApplicationContext;
import ru.itis.some.project.services.ConfirmSignUpService;
import ru.itis.some.project.util.exceptions.ServiceException;
import ru.itis.some.project.util.servlets.BetterHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/signUp/confirm")
public class ConfirmSignUpServlet extends BetterHttpServlet {
    private ConfirmSignUpService service;

    @Override
    protected void init(ApplicationContext context) {
        service = context.getBean(ConfirmSignUpService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var token = helper.getParameter(req, "token");

        try {
            if (service.confirm(token)) {
                helper.sendView(req, resp, "sign_up_confirmed");
            } else {
                resp.sendRedirect("/");
            }
        } catch (ServiceException e) {
            resp.sendError(404);
        }
    }
}
