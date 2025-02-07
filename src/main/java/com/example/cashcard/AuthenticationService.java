package com.example.cashcard;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "";
    private static final String AUTH_TOKEN = "";

    public static Authentication getAuthentication(HttpServletRequest request) {
//    	
//    	
//    	System.out.println("Request Method: " + request.getMethod());
//        System.out.println("Request URI: " + request.getRequestURI());
//        System.out.println("Request Protocol: " + request.getProtocol());
//        
//        System.out.println("Remote Addr: " + request.getRemoteAddr());
//        System.out.println("Remote Host: " + request.getRemoteHost());
//        System.out.println("Remote Port: " + request.getRemotePort());
//        
//        System.out.println("Context Path: " + request.getContextPath());
//        System.out.println("Servlet Path: " + request.getServletPath());
//        System.out.println("Path Info: " + request.getPathInfo());
//        System.out.println("Query String: " + request.getQueryString());
        
        System.out.println("Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }

        System.out.println("Parameters:");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println(paramName + ": " + request.getParameter(paramName));
        }
        
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}