package com.nathansakkriou

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = MoulinetteConstants.PROPERTIES_PREFIX)
class MoulinetteProperties(
    val message: String,
    val csvPersistFilePath: String,
    val enable: Boolean,
)