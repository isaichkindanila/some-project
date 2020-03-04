package ru.itis.some.project.util.servlets;

import org.springframework.context.ApplicationContext;
import ru.itis.some.project.util.exceptions.RequestException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BetterHttpServlet extends HttpServlet {
    protected ServletHelper helper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        var servletContext = config.getServletContext();
        var springContext = (ApplicationContext) servletContext.getAttribute("springContext");

        helper = springContext.getBean(ServletHelper.class);
        init(springContext);
    }

    protected abstract void init(ApplicationContext context);

    @Override
    protected final void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            super.service(req, resp);
        } catch (RequestException e) {
            resp.sendError(400, e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            resp.sendError(500);
        }
    }
}
