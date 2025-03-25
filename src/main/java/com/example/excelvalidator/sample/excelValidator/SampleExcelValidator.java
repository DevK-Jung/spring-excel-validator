package com.example.excelvalidator.sample.excelValidator;

import com.example.excelvalidator.excel.validator.ExcelRowValidator;
import com.example.excelvalidator.excel.validator.vo.ExcelErrorVo;
import com.example.excelvalidator.sample.dto.SampleExcelDto;
import io.micrometer.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link SampleExcelDto}의 각 행 데이터를 검증하는 Validator 구현체입니다.
 * <p>
 * 필수 값 누락, 이메일 형식, 날짜 형식 등을 검사하며,
 * 오류가 발생한 경우 {@link ExcelErrorVo} 리스트로 반환합니다.
 */
public class SampleExcelValidator implements ExcelRowValidator<SampleExcelDto> {
    @Override
    public List<ExcelErrorVo> validate(SampleExcelDto row, int rowNumber) {
        List<ExcelErrorVo> errors = new ArrayList<>();

        if (StringUtils.isBlank(row.getUsername())) {
            errors.add(new ExcelErrorVo(rowNumber, "username", "아이디는 필수입니다."));
        }

        if (StringUtils.isBlank(row.getEmail()) || !row.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.add(new ExcelErrorVo(rowNumber, "email", "이메일 형식이 올바르지 않습니다."));
        }

        if (StringUtils.isBlank(row.getBirthdate()) || !row.getBirthdate().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            errors.add(new ExcelErrorVo(rowNumber, "birthdate", "날짜 형식은 yyyy-MM-dd 입니다."));
        }

        return errors;
    }
}
