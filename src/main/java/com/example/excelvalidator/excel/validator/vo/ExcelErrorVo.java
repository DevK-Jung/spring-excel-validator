package com.example.excelvalidator.excel.validator.vo;

/**
 * 엑셀 데이터 검증 실패 시 발생한 오류 정보를 담는 VO 클래스입니다.
 * <p>
 * 각 행(row)마다 필드(fieldName) 단위로 어떤 에러가 발생했는지를 나타냅니다.
 *
 * @param rowNumber    엑셀에서 오류가 발생한 행 번호 (1부터 시작)
 * @param fieldName    오류가 발생한 필드명 (예: "username", "email")
 * @param errorMessage 사용자에게 보여줄 에러 메시지
 */
public record ExcelErrorVo(int rowNumber, String fieldName, String errorMessage) {
}

