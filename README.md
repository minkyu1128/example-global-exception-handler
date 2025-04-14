# 📘 Exception 및 Response 구조 통합 가이드

## 1. 왜 이 방식이 필요한가요?

서비스 개발 과정에서 예외 처리와 응답 형식이 일관되지 않으면 다음과 같은 문제가 발생할 수 있습니다:

- 예외 상황마다 응답 포맷이 달라 클라이언트에서 파싱이 어려움
- 예외 코드 관리가 흩어져 있어 유지보수 비용 증가
- 새로운 예외 상황 추가 시 중복 구현 발생

> 이러한 문제들을 해결하기 위해, 본 프로젝트에서는 예외와 응답을 일원화하는 방식을 도입했습니다.

---

## 2. 제안하는 구조

### ✅ 예외 처리 방식

- 모든 예외는 `DefaultException`을 상속하여 처리
- 예외 정보는 `IError` 인터페이스 기반의 `enum`으로 정의하여 코드와 메시지 및 HttpStatus 통합 관리

### ✅ Response 구조

- 정상 응답: `DefaultResponse<T>`
- 실패 응답: `DefaultErrorResponse`
- 두 클래스 모두 `IResponse`를 구현하여 통일된 포맷 제공

---

## 3. 주요 장점

| 항목	                | 일원화 전	            | 일원화 후              |
|--------------------|-------------------|--------------------|
| 예외 메시지 관리          | 	중복 및 분산          | Enum 기반 중앙 집중      |
| 응답 구조 일관성	         | ❌	                | ✅                  |
| 테스트 작성 용이성         | 	다양한 케이스 고려 필요	   | 응답 구조 고정으로 단순      |
| 유지보수 효율(추적, 변경 등)	 | 흐름 파악 어려움	        | 예외-응답 흐름 명확        |
| 국제화(i18n) 적용       | 	어려움 (메시지 분산)     | 	쉬움 (Enum으로 통합 관리) |
| 클라이언트 파싱 편의        | 	복잡 (응답 포맷 다양)	   | 단순 (단일 포맷)         |
| 공통 정책 적용 용이성	      | 어려움 (각 응답별 별도 처리) | 	쉬움 (인터페이스 기반)     |
| Swagger/문서화 편의성	   | 응답 형태 다양          | 	일관된 구조로 자동 문서화 쉬움 |

---

## 4. 예시 코드

**Controller 레이어**

```kotlin
@PostMapping("/datas")
fun data(@Valid @RequestBody request: SampleRequest): ResponseEntity<IResponse> {
    return DefaultResponse.ok()
}
```

**Service 레이어**

```kotlin
if (data == null) {
    throw DefaultException(SampleError.REGISTERED_DATA_NOT_FOUND)
}

//또는
sampleRepository.findById(id).orElseThrow { DefaultException(SampleError.REGISTERED_DATA_NOT_FOUND) }
```

**에러 코드**

```kotlin

enum class SampleError(private val httpStatus: HttpStatus, private val message: String) : IError {
    REGISTERED_DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 데이터가 없습니다");

    override fun status(): HttpStatus {
        return this.httpStatus
    }
    override fun getCode(): String {
        return this.name
    }
    override fun getMessage(): String {
        return this.message
    }
}
```

--- 

## 5. 테스트

### 📌 정상 응답

* **[HttpStatus]:** 200 OK (고정)
* **[ErrorCode]:** OK (고정)

요청

```
gradlew :test --tests "com.example.exampleglobalexceptionhandler.controller.SampleControllerTest.데이터 단건 조회 - 특정 ID로 데이터를 요청하면 상세정보 응답 200 OK"
```

응답

``` json
{
  "code": "OK",
  "message": "Success",
  "timestamp": 1744623583390,
  "data": {
    "id": "123",
    "title": "Hello World",
    "description": "Welcome to the world"
  }
}
```

### 📌 실패 응답 - 비즈니스 예외 (사용자정의 에러코드 및 HttpStatus)

* **[HttpStatus]:** 사용자 정의
* **[ErrorCode]:** 사용자 정의

``` 
gradlew :test --tests "com.example.exampleglobalexceptionhandler.controller.SampleControllerTest.데이터 수정 - 등록된 데이터가 없어 404 Not Found"
```

응답

``` json
{
  "code": "REGISTERED_DATA_NOT_FOUND",
  "message": "등록된 데이터가 없습니다",
  "timestamp": 1744623583375
}
```

### 📌 실패 응답 - 잘못된 요청 시 (BeanValidation 검증 실패)

* **[HttpStatus]:** 401 Bad Request (고정)
* **[ErrorCode]:** INVALID_FIELDS (고정)

요청

```
gradlew :test --tests "com.example.exampleglobalexceptionhandler.controller.SampleControllerTest.데이터 등록 - 제목이 20자를 초과하면 400 Bad Request"
```

응답

```json
{
  "code": "INVALID_FIELDS",
  "message": "유효하지 않은 필드 값 입니다",
  "timestamp": 1744625277534,
  "errors": [
    "제목은 20자 이내로 입력하셔야 합니다"
  ]
}
```

---

## 6. 결론

이 방식은 실제 현업에서도 널리 사용되며, 특히 **대규모 서비스**에서 예외 처리와 응답 포맷의 일관성 확보에 매우 효과적입니다.   
유지보수성과 확장성을 고려했을 때, 본 구조는 향후 서비스 고도화에도 큰 도움이 됩니다.
