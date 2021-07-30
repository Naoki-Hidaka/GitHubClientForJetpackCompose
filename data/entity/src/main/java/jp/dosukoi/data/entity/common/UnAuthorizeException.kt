package jp.dosukoi.data.entity.common

class UnAuthorizeException(val errorMessage: String) : RuntimeException(errorMessage)