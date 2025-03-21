package com.example.excelvalidator.sample.service;

import com.example.excelvalidator.core.exceptions.ExcelValidateException;
import com.example.excelvalidator.sample.dto.SampleExcelReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SampleServiceTest {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 엑셀_검증_에러_발생_테스트_검증_구현체() throws Exception {
        // given
        ClassPathResource resource = new ClassPathResource("sample-excel-request.json");
        String json = Files.readString(Path.of(resource.getURI()), StandardCharsets.UTF_8);
        SampleExcelReqDto dto = objectMapper.readValue(json, SampleExcelReqDto.class);

        // when & then
        ExcelValidateException exception = assertThrows(
                ExcelValidateException.class,
                () -> sampleService.excelValidateSampleFromImplementation(dto) // 메서드명에 맞게 변경
        );

        assertNotNull(exception.getErrors());
        assertEquals(3, exception.getErrors().size());

        exception.getErrors().forEach(err ->
                System.out.printf("Row %d - %s : %s%n", err.rowNumber(), err.fieldName(), err.errorMessage())
        );
    }

    @Test
    void 엑셀_검증_에러_발생_테스트_검증_람다() throws Exception {
        // given
        ClassPathResource resource = new ClassPathResource("sample-excel-request.json");
        String json = Files.readString(Path.of(resource.getURI()), StandardCharsets.UTF_8);
        SampleExcelReqDto dto = objectMapper.readValue(json, SampleExcelReqDto.class);

        // when & then
        ExcelValidateException exception = assertThrows(
                ExcelValidateException.class,
                () -> sampleService.excelValidateSampleFromLamda(dto) // 메서드명에 맞게 변경
        );

        assertNotNull(exception.getErrors());
        assertEquals(3, exception.getErrors().size());

        exception.getErrors().forEach(err ->
                System.out.printf("Row %d - %s : %s%n", err.rowNumber(), err.fieldName(), err.errorMessage())
        );
    }
}