package ru.shipa.app.data.exception

abstract class ServerException(
    val code: String,
    val description: String,
    val title: String
) : RuntimeException(description)
