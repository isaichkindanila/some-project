package ru.itis.some.project.servlets;

import org.springframework.context.ApplicationContext;
import ru.itis.some.project.dto.SignUpDto;
import ru.itis.some.project.services.SignUpService;
import ru.itis.some.project.util.exceptions.ServiceException;
import ru.itis.some.project.util.servlets.BetterHttpServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/signUp")
public class SignUpServlet extends BetterHttpServlet {
    private SignUpService signUpService;

    @Override
    protected void init(ApplicationContext context) {
        signUpService = context.getBean(SignUpService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var error = req.getParameter("err");

        if (error == null) {
            helper.sendView(req, resp, "sign_up");
        } else {
            var modelMap = Map.of("error", req.getParameter("err"));
            helper.sendView(req, resp, "sign_up", modelMap);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var dto = SignUpDto.builder()
                .email(helper.getParameter(req, "email"))
                .password(helper.getParameter(req, "password"))
                .build();

        try {
            signUpService.signUp(dto);

            var modelMap = Map.of("email", dto.getEmail());
            helper.sendView(req, resp, "sign_up_confirm", modelMap);
        } catch (ServiceException e) {
            resp.sendRedirect("/signUp?err=" + e.getMessage());
        }
    }
}
