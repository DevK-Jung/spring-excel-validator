package com.example.excelvalidator.excel.validator;

import com.example.excelvalidator.excel.validator.vo.ExcelErrorVo;

import java.util.List;


/**
 * 엑셀 데이터의 각 행에 대한 유효성 검증을 수행하는 함수형 인터페이스입니다.
 * <p>
 * {@link ExcelValidator} 클래스에서 사용되며, 각 행(row)에 대해 검증 로직을 구현할 수 있습니다.
 *
 * @param <T> 엑셀의 한 행을 표현하는 DTO 타입
 */
@FunctionalInterface
public interface ExcelRowValidator<T> {
    List<ExcelErrorVo> validate(T row, int rowNumber);
}
