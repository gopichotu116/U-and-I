package com.org.ui.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception ex) {
//        return new ResponseEntity<>("Database have this Credential already: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        return new ResponseEntity<>("<h1 style='text-align: center'>Database have this Credential already</h1> " , HttpStatus.INTERNAL_SERVER_ERROR);
		String htmlContent = "<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>Error</title>\r\n" + "    <style>\r\n" + "        body {\r\n"
				+ "            font-family: Arial, sans-serif;\r\n" + "            background-color: #f8f9fa;\r\n"
				+ "            margin: 0;\r\n" + "            padding: 0;\r\n" + "            display: flex;\r\n"
				+ "            justify-content: center;\r\n" + "            align-items: center;\r\n"
				+ "            height: 70vh;\r\n" + "        }\r\n" + "        .container {\r\n"
				+ "            text-align: center;\r\n" + "            padding: 20px;\r\n"
				+ "            border-radius: 5px;\r\n" + "            background-color: #ffffff;\r\n"
				+ "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\r\n" + "        }\r\n" + "        h1 {\r\n"
				+ "            color: #dc3545;\r\n" + "        }\r\n" + "        p {\r\n"
				+ "            color: #6c757d;\r\n" + "        }\r\n" + "    </style>\r\n" + "</head>\r\n"
				+ "<body>\r\n" + "    <div class=\"container\">\r\n" + "        <h1>Error Page..!</h1>\r\n"
				+ "        <p>Something went wrong please try again</p>\r\n" + "    </div>\r\n" + "</body>\r\n"
				+ "</html>\r\n" + "";
		
		
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(htmlContent);
		return ResponseEntity.status(HttpStatus.OK).body(htmlContent);
	}
}
