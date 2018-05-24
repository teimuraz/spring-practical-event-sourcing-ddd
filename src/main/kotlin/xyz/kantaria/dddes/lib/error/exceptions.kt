package xyz.kantaria.dddes.lib.error

open class ApplicationException(message: String) : RuntimeException(message)
open class ValidationException(message: String) : ApplicationException(message)
open class ForbiddenException(message: String) : ApplicationException(message)
open class InternalErrorException(message: String) : ApplicationException(message)
