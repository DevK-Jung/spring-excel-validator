package com.example.excelvalidator.core.exceptions;

import com.example.excelvalidator.excel.validator.vo.ExcelErrorVo;

import java.util.List;

/**
 * 엑셀 업로드 시 유효성 검증에 실패했을 때 발생하는 예외입니다.
 * <p>
 * 각 행(Row) 단위로 발생한 필드별 오류 목록을 {@link ExcelErrorVo} 리스트로 포함합니다.
 * 이 예외는 {@code @ControllerAdvice}에서 캐치하여 클라이언트에게 오류 정보를 응답으로 전달할 수 있습니다.
 */
public class ExcelValidateException extends RuntimeException {

    private final List<ExcelErrorVo> errors;

    public ExcelValidateException(List<ExcelErrorVo> errors) {
        super("엑셀 유효성 검증 실패");
        this.errors = errors;
    }

    public List<ExcelErrorVo> getErrors() {
        return errors;
    }
}
