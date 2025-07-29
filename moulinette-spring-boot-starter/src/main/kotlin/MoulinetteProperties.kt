package com.nathansakkriou

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "moulinette")
class MoulinetteProperties(
    val message: String,
    val csvPersistFilePath: String,
)