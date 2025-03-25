package com.example.excelvalidator.excel.parser.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    int index();       // 엑셀 컬럼 인덱스 (0부터 시작)
}
