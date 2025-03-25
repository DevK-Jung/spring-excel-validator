package com.example.excelvalidator.sample.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SampleExcelReqDto {
    private List<SampleExcelDto> list;
}
