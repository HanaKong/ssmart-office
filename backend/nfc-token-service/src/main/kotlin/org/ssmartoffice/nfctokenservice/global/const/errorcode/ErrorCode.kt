package org.ssmartoffice.nfctokenservice.global.const.errorcode


import org.springframework.http.HttpStatus
import java.io.Serializable

interface ErrorCode : Serializable {
    val httpStatus: HttpStatus
    val message: String
    val name: String
}
