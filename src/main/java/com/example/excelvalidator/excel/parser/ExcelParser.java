package com.example.excelvalidator.excel.parser;

import com.example.excelvalidator.excel.parser.annotations.ExcelColumn;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ExcelParser {

    public <T> List<T> parse(MultipartFile file, Class<T> type) {
        List<T> result = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Field[] fields = type.getDeclaredFields();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                T instance = type.getDeclaredConstructor().newInstance();

                for (Field field : fields) {
                    if (!field.isAnnotationPresent(ExcelColumn.class)) continue;

                    ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                    int columnIndex = annotation.index();

                    Cell cell = row.getCell(columnIndex);
                    Object cellValue = getCellValue(cell);

                    field.setAccessible(true);
                    field.set(instance, cellValue);
                }

                result.add(instance);
            }

        } catch (Exception e) {
            throw new RuntimeException("엑셀 파싱 중 오류 발생", e);
        }

        return result;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate(); // LocalDate 반환
                } else {
                    double numericValue = cell.getNumericCellValue();
                    // 정수로 떨어지면 int로 캐스팅
                    if (numericValue == Math.floor(numericValue)) {
                        return (int) numericValue;
                    } else {
                        return numericValue;
                    }
                }

            case BOOLEAN:
                return cell.getBooleanCellValue();

            case FORMULA:
                return evaluateFormulaCell(cell);

            case BLANK:
            default:
                return null;
        }
    }

    private Object evaluateFormulaCell(Cell cell) {
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        CellValue evaluated = evaluator.evaluate(cell);

        return switch (evaluated.getCellType()) {
            case STRING -> evaluated.getStringValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate();
                }
                yield evaluated.getNumberValue();
            }
            case BOOLEAN -> evaluated.getBooleanValue();
            default -> null;
        };
    }
}
