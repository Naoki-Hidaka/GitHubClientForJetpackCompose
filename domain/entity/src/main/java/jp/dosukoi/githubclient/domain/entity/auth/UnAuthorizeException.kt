package jp.dosukoi.githubclient.domain.entity.auth

class UnAuthorizeException(val errorMessage: String) : RuntimeException(errorMessage)
