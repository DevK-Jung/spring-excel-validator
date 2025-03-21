# Excel Validator for Spring Boot

Spring Boot 기반의 엑셀 업로드 유효성 검증 공통 유틸리티입니다.  
검증 로직을 공통화하고, 업무별로 검증 규칙을 유연하게 주입할 수 있도록 설계되었습니다.

---

## 주요 기능

- Excel 데이터의 행 단위 유효성 검증
- 업무별 검증 로직을 람다 또는 Validator 구현체로 주입 가능
- 유효성 실패 시 예외 발생 및 전역 처리
- 에러 정보는 JSON 형태로 프론트에 반환
- 재사용 가능한 구조와 단위 테스트 기반 검증

---

## 프로젝트 구조

<details>
<summary>디렉토리 트리</summary>

```
+---main
|   +---java
|   |   \---com
|   |       \---example
|   |           \---excelvalidator
|   |               |   ExcelValidatorApplication.java
|   |               |
|   |               +---core
|   |               |   +---exceptionHandler
|   |               |   |       GlobalExceptionHandler.java
|   |               |   |
|   |               |   \---exceptions
|   |               |           ExcelValidateException.java
|   |               |
|   |               +---excel
|   |               |   +---validator
|   |               |   |       ExcelRowValidator.java
|   |               |   |       ExcelValidator.java
|   |               |   |
|   |               |   \---vo
|   |               |           ExcelErrorVo.java
|   |               |
|   |               \---sample
|   |                   +---controller
|   |                   |       SampleController.java
|   |                   |
|   |                   +---dto
|   |                   |       SampleExcelDto.java
|   |                   |       SampleExcelReqDto.java
|   |                   |
|   |                   +---excelValidator
|   |                   |       SampleExcelValidator.java
|   |                   |
|   |                   \---service
|   |                           SampleService.java
|   |
|   \---resources
|       |   application.properties
|       |
|       +---static
|       \---templates
\---test
    +---java
    |   \---com
    |       \---example
    |           \---excelvalidator
    |               |   ExcelValidatorApplicationTests.java
    |               |
    |               \---sample
    |                   \---service
    |                           SampleServiceTest.java
    |
    \---resources
            sample-excel-request.json
```
</details>

---

## 예외 및 에러 처리 구조

- 모든 유효성 검사는 `ExcelValidator.validateAll()`에서 수행
- 실패 시 `ExcelValidateException` 발생
- `@ControllerAdvice`로 예외를 잡고 `List<ExcelErrorVo>` 반환

### 예외 응답 예시

```json
[
  {
    "rowNumber": 3,
    "fieldName": "username",
    "errorMessage": "아이디는 필수입니다."
  },
  {
    "rowNumber": 4,
    "fieldName": "email",
    "errorMessage": "이메일 형식이 올바르지 않습니다."
  }
]
```

### 사용 예시

#### 람다 방식

```java
ExcelValidator.validateAll(excelList, (row, rowNum) -> {
    List<ExcelErrorVo> errors = new ArrayList<>();
    if (StringUtils.isBlank(row.getUsername())) {
        errors.add(new ExcelErrorVo(rowNum, "username", "아이디는 필수입니다."));
    }
    return errors;
});
```

#### 구현체 방식

```java
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
```

```java
SampleExcelValidator validator = new SampleExcelValidator();
ExcelValidator.validateAll(excelList, validator);
```

---

## 테스트 실행

```bash
./gradlew test
```

---

## 테스트용 JSON 위치
테스트에서 사용하는 예제 JSON은 다음 위치에 있습니다:

```bash
src/test/resources/sample-excel-request.json
```

---

## 작성자
김정현 ([@DevK-Jung](https://github.com/DevK-Jung))

---

## 라이선스
MIT License
