package com.example.excelvalidator.sample.controller;

import com.example.excelvalidator.sample.dto.SampleExcelDto;
import com.example.excelvalidator.sample.dto.SampleExcelReqDto;
import com.example.excelvalidator.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/samples/excel")
public class SampleController {

    private final SampleService sampleService;

    @PostMapping("/impl")
    public void excelValidateSampleFromImplementation(@RequestBody SampleExcelReqDto param) {
        sampleService.excelValidateSampleFromImplementation(param.getList());
    }

    @PostMapping("/lamda")
    public void excelValidateSampleFromLamda(@RequestBody SampleExcelReqDto param) {
        sampleService.excelValidateSampleFromLamda(param.getList());
    }

    @PostMapping(value = "/parse",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<SampleExcelDto> excelDataParse(@RequestPart("excel") MultipartFile excel) {
        return sampleService.excelDataParse(excel);
    }
}
