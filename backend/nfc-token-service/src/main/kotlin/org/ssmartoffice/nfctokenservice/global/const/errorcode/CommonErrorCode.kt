package org.ssmartoffice.nfctokenservice.global.const.errorcode


import org.springframework.http.HttpStatus

enum class CommonErrorCode(override val httpStatus: HttpStatus, override val message: String) : ErrorCode {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 parameter가 포함되었습니다."),
    INVALID_METHOD_ARG(HttpStatus.BAD_REQUEST, "요청 본문에 잘못된 값이 포함되어 있습니다. 필드를 확인해주세요."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "요청 본문을 읽을 수 없습니다. 올바른 JSON 형식인지 확인해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러 입니다.")
    ;
}
