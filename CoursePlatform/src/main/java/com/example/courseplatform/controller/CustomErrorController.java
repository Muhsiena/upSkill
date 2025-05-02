package com.example.courseplatform.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        StringBuilder errorMessage = new StringBuilder("An unexpected error occurred.");
        if (status != null) {
            errorMessage = new StringBuilder("Status Code: " + status);
        }
        if (message != null) {
            errorMessage.append("<br/>Message: ").append(message);
        }
        if (exception != null) {
            errorMessage.append("<br/>Exception: ").append(exception);
        }

        model.addAttribute("errorMessage", errorMessage.toString());
        return "error";
    }
}
