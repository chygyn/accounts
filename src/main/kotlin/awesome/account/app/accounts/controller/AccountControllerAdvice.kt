package awesome.account.app.accounts.controller

import awesome.account.app.accounts.exception.ProcessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class AccountControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleProcessException(e: RuntimeException, request: WebRequest): ResponseEntity<ProcessExceptionDetails> {
        when (e) {
            is ProcessException -> {
                return ResponseEntity(
                    ProcessExceptionDetails(message = e.fault), HttpStatus.CONFLICT
                )
            }
            is IllegalArgumentException -> {
                return ResponseEntity(
                    ProcessExceptionDetails(message = e.message), HttpStatus.BAD_REQUEST
                )
            }
            else -> {
                return ResponseEntity(
                    ProcessExceptionDetails(message = "Something going wrong"), HttpStatus.INTERNAL_SERVER_ERROR
                )
            }
        }
    }

}

class ProcessExceptionDetails(
    val message: String?
)