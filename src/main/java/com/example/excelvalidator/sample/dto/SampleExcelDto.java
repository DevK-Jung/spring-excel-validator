package com.example.excelvalidator.sample.dto;

import com.example.excelvalidator.excel.parser.annotations.ExcelColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SampleExcelDto {
    @ExcelColumn(index = 0)
    private String username;
    @ExcelColumn(index = 1)
    private String email;
    @ExcelColumn(index = 2)
    private String birthdate;
}
