package com.example.excelvalidator.sample.service;

import com.example.excelvalidator.excel.parser.ExcelParser;
import com.example.excelvalidator.excel.validator.ExcelValidator;
import com.example.excelvalidator.excel.validator.vo.ExcelErrorVo;
import com.example.excelvalidator.sample.dto.SampleExcelDto;
import com.example.excelvalidator.sample.excelValidator.SampleExcelValidator;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 샘플 엑셀 업로드 데이터에 대한 유효성 검증 로직을 제공하는 서비스 클래스입니다.
 * <p>
 * {@link ExcelValidator}를 활용하여 구현체 기반 또는 람다 기반으로 유효성 검증을 수행할 수 있습니다.
 */
@Service
public class SampleService {

    /**
     * {@link SampleExcelValidator} 구현체를 사용하여 엑셀 데이터를 검증합니다.
     *
     * @param list 엑셀 요청 데이터
     * @throws com.example.excelvalidator.core.exceptions.ExcelValidateException 유효성 검증 실패 시 예외가 발생하며, 클라이언트에 에러 리스트가 반환됩니다.
     */
    public void excelValidateSampleFromImplementation(List<SampleExcelDto> list) { // 구현체를 활용한 엑셀 업로드
        SampleExcelValidator excelValidator = new SampleExcelValidator();

        ExcelValidator.validateAll(list, excelValidator);
    }

    /**
     * 람다 표현식을 사용하여 Sample 엑셀 데이터를 검증합니다.
     * <p>
     * username, email, birthdate 필드의 유효성을 직접 정의한 인라인 검증 로직입니다.
     *
     * @param list 엑셀 요청 데이터
     * @throws com.example.excelvalidator.core.exceptions.ExcelValidateException 유효성 검증 실패 시 예외가 발생합니다.
     */
    public void excelValidateSampleFromLamda(List<SampleExcelDto> list) { // 구현체를 활용한 엑셀 업로드

        ExcelValidator.validateAll(list, (row, rowNumber) -> {
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
        });
    }

    public List<SampleExcelDto> excelDataParse(MultipartFile excel) {

        return ExcelParser.parse(excel, SampleExcelDto.class);
    }
}
