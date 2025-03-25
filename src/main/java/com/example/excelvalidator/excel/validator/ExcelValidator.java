package com.example.excelvalidator.excel.validator;

import com.example.excelvalidator.core.exceptions.ExcelValidateException;
import com.example.excelvalidator.excel.validator.vo.ExcelErrorVo;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 엑셀 데이터 검증을 위한 공통 유틸리티 클래스입니다.
 * <p>
 * {@code ExcelRowValidator<T>}를 구현한 검증 로직을 기반으로
 * 엑셀 파싱 결과 리스트에 대해 일괄 검증을 수행하고,
 * 유효성 오류가 존재할 경우 {@link ExcelValidateException}을 발생시킵니다.
 */
@UtilityClass
public class ExcelValidator {

    /**
     * 엑셀 데이터를 검증합니다.
     *
     * @param rows      엑셀에서 파싱된 데이터 리스트
     * @param validator 각 행에 대한 검증 로직을 구현한 {@link ExcelRowValidator}
     * @param <T>       엑셀 행 데이터 타입
     * @throws ExcelValidateException 유효성 검증 실패 시 예외를 발생시키며, 에러 목록을 포함합니다.
     */
    public <T> void validateAll(List<T> rows, ExcelRowValidator<T> validator) {
        List<ExcelErrorVo> errors = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            T row = rows.get(i);
            int rowNumber = i + 1;
            List<ExcelErrorVo> rowErrors = validator.validate(row, rowNumber);
            
            if (!CollectionUtils.isEmpty(rowErrors)) errors.addAll(rowErrors);
        }

        if (!errors.isEmpty())
            throw new ExcelValidateException(errors);
    }
}
