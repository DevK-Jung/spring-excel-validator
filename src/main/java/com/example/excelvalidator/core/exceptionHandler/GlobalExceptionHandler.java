package com.example.excelvalidator.core.exceptionHandler;

import com.example.excelvalidator.core.exceptions.ExcelValidateException;
import com.example.excelvalidator.excel.validator.vo.ExcelErrorVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 엑셀 유효성 검증 실패 시 발생하는 {@link ExcelValidateException} 예외를 처리합니다.
     *
     * @param ex 발생한 예외
     * @return {@link ExcelErrorVo} 리스트를 포함한 400 Bad Request 응답
     */
    @ExceptionHandler(ExcelValidateException.class)
    public ResponseEntity<List<ExcelErrorVo>> excelValidateException(ExcelValidateException ex) {

        return ResponseEntity.badRequest().body(ex.getErrors());
    }

}
