package com.xmap.v04.exception;
import lombok.extern.slf4j.Slf4j;
import com.xmap.v04.ResultEntiy.ResultEnum;
import com.xmap.v04.ResultEntiy.BusinessException;
import com.xmap.v04.ResultEntiy.ResultResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    /**
//     * 处理自定义异常
//     *
//     */
//    @ExceptionHandler(value = BusinessException.class)
//    public Object customExceptionHandler(BusinessException e){
//        log.error(e.getMessage(), e);
//        return new ResultResponse<>(e.getErrorCode(),e.getMessage());
//        //return ResultResponse.customException(e);
//    }




//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @ResponseBody
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ApiCallError<String>> handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
//
//        logger.error("NotFoundException {}\n", request.getRequestURI(), ex);
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .body(new ApiCallError<>("Not found exception", List.of(ex.getMessage())));
//    }
//
//
//    @ExceptionHandler(ParamsMissException.class)
//    public ResponseEntity<ApiCallError<String>> handleParamsMissException(HttpServletRequest request, ParamsMissException ex) {
//        logger.error("ParamsMissException {}\n", request.getRequestURI(), ex);
//
//        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
//                .body(new ApiCallError<>("Missing request parameter", List.of(ex.getMessage())));
//    }
//
//    @ExceptionHandler(BaseException.class)
//    public ResponseEntity<ApiCallError<String>> handleBaseException(HttpServletRequest request, BaseException ex) {
//        logger.error("Base Exception {}\n", request.getRequestURI(), ex);
//
//        return ResponseEntity.badRequest().body(new ApiCallError<>("conflict request", List.of(ex.getMessage())));
//    }
//
//    @ExceptionHandler(InternalAuthenticationServiceException.class)
//    public ResponseEntity<ApiCallError<String>> handleInternalAuthenticationServiceException(HttpServletRequest request, InternalAuthenticationServiceException ex) {
//        logger.error("handleInternalAuthenticationServiceException {}\n", request.getRequestURI(), ex);
//
//
//        return ResponseEntity
//                .status(HttpStatus.NOT_ACCEPTABLE)
//                .body(new ApiCallError<>("Authentication error, username incorrect.", List.of(ex.getMessage())));
//    }
//
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ApiCallError<String>> handleBadCredentialsException(HttpServletRequest request, BadCredentialsException ex) {
//        logger.error("handleBadCredentialsException {}\n", request.getRequestURI(), ex);
//
//
//        return ResponseEntity
//                .badRequest()
//                .body(new ApiCallError<>("Authentication error, password incorrect.", List.of(ex.getMessage())));
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiCallError<Map<String, String>>> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex) {
//        logger.error("handleMethodArgumentNotValidException {}\n", request.getRequestURI(), ex);
//
//        List<Map<String, String>> details = new ArrayList<>();
//        ex.getBindingResult()
//                .getFieldErrors()
//                .forEach(fieldError -> {
//                    Map<String, String> detail = new HashMap<>();
//                    detail.put("objectName", fieldError.getObjectName());
//                    detail.put("field", fieldError.getField());
//                    detail.put("rejectedValue", "" + fieldError.getRejectedValue());
//                    detail.put("errorMessage", fieldError.getDefaultMessage());
//                    details.add(detail);
//                });
//
//        return ResponseEntity
//                .badRequest()
//                .body(new ApiCallError<>("Method argument validation failed", details));
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ApiCallError<String>> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex) {
//        logger.error("handleAccessDeniedException {}\n", request.getRequestURI(), ex);
//
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .body(new ApiCallError<>("Access denied!", List.of(ex.getMessage())));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiCallError<String>> handleInternalServerError(HttpServletRequest request, Exception ex) {
//        logger.error("handleInternalServerError {}\n", request.getRequestURI(), ex);
//
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(new ApiCallError<>("Internal server error", List.of(ex.getMessage())));
//    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class ApiCallError<T> {
//
//        private String message;
//        private List<T> details;
//
//    }
}
