package com.softawii.social.controller;

import com.softawii.social.config.AppConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    private final AppConfig appConfig;

    public ExceptionHandlingController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        // Criar uma resposta de erro personalizada
        String mensagemErro = "Formato de número inválido. Certifique-se de fornecer um valor numérico válido.";
        return new ResponseEntity<>(mensagemErro, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName    = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(Throwable.class)
    public ModelAndView majorHandler(HttpServletRequest req, Throwable e) {
        ModelAndView mav = new ModelAndView();
        if (appConfig.isProduction()) {
            mav.setViewName("error.html");
            return mav;
        }

        mav.addObject("url", req.getRequestURL());
        mav.addObject("exception", e);

        StringWriter sw = new StringWriter();
        PrintWriter  pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        mav.addObject("trace", sw.toString());
        mav.setViewName("dev-error.html");

        return mav;
    }
}