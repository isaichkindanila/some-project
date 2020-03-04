package ru.itis.some.project.util.servlets;

import org.springframework.stereotype.Component;
import ru.itis.some.project.util.exceptions.RequestException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServletHelper {

    public void sendView(HttpServletRequest request,
                         HttpServletResponse response,
                         String view) throws ServletException, IOException {
        sendView(request, response, view, new HashMap<>());
    }

    public void sendView(HttpServletRequest request,
                         HttpServletResponse response,
                         String view,
                         Map<String, ?> models) throws ServletException, IOException {

        for (var model : models.entrySet()) {
            request.setAttribute(model.getKey(), model.getValue());
        }

        var url = "/" + view + ".ftl";
        request.getRequestDispatcher(url).forward(request, response);
    }

    public String getParameter(HttpServletRequest request, String name) {
        var param = request.getParameter(name);

        if (param == null) {
            throw new RequestException("missing required parameter: '" + name + "'");
        }

        return param;
    }
}
