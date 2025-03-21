package com.example.excelvalidator.sample.service;

import com.example.excelvalidator.core.exceptions.ExcelValidateException;
import com.example.excelvalidator.sample.dto.SampleExcelReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SampleServiceTest {

    @Autowired
    private SampleService sampleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 엑셀_검증_에러_발생_테스트_검증_구현체() throws Exception {
        // given
        ClassPathResource resource = new ClassPathResource("sample-excel-request.json");
        String json = Files.readString(Path.of(resource.getURI()), StandardCharsets.UTF_8);
        SampleExcelReqDto dto = objectMapper.readValue(json, SampleExcelReqDto.class);

        // when & then
        ExcelValidateException exception = assertThrows(
                ExcelValidateException.class,
                () -> sampleService.excelValidateSampleFromImplementation(dto.getList()) // 메서드명에 맞게 변경
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
                () -> sampleService.excelValidateSampleFromLamda(dto.getList()) // 메서드명에 맞게 변경
        );

        assertNotNull(exception.getErrors());
        assertEquals(3, exception.getErrors().size());

        exception.getErrors().forEach(err ->
                System.out.printf("Row %d - %s : %s%n", err.rowNumber(), err.fieldName(), err.errorMessage())
        );
    }

    @Test
    void generateSampleExcel() throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Samples");

        // 헤더
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("username");
        header.createCell(1).setCellValue("email");
        header.createCell(2).setCellValue("birthdate");

        // 데이터 (10건 중 3건 오류 포함)
        String[][] data = {
                {"john_doe", "john.doe@example.com", "1990-05-10"},
                {"jane_smith", "jane.smith@example.com", "1985-12-01"},
                {"", "blank.username@example.com", "1991-07-21"},              // username 없음
                {"invalid_email", "not-an-email", "1992-03-15"},               // 이메일 형식 오류
                {"missing_birth", "mb@example.com", ""},                       // 생년월일 없음
                {"alice", "alice@example.com", "1999-08-08"},
                {"bob", "bob_1992@example.com", "1992-11-30"},
                {"charlie", "charlie@example.com", "2000-01-01"},
                {"diana", "diana.k@example.com", "1988-09-15"},
                {"edward", "edward.l@example.com", "1993-06-25"},
        };

        for (int i = 0; i < data.length; i++) {
            Row row = sheet.createRow(i + 1);
            for (int j = 0; j < data[i].length; j++) {
                row.createCell(j).setCellValue(data[i][j]);
            }
        }

        File file = new File("src/test/resources/sample-upload.xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }

        workbook.close();
        System.out.println("Excel 파일 생성 완료: " + file.getAbsolutePath());
    }

    @Test
    void excel_파싱_API_테스트() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "excel",
                "sample-upload.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                Files.readAllBytes(Path.of("src/test/resources/sample-upload.xlsx"))
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/samples/excel/parse")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(result -> {
                    System.out.println("응답 결과: " + result.getResponse().getContentAsString());
                });
    }

}