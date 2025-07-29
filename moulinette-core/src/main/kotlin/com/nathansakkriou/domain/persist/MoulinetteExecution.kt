package com.nathansakkriou.domain.persist

import com.nathansakkriou.domain.identification.MoulinetteIdentification
import java.time.LocalDateTime

class MoulinetteExecution(
    private val identification: MoulinetteIdentification,
    private val success: Boolean,
    ) {

    fun getIdentification(): MoulinetteIdentification {
        return identification
    }

    fun isSuccess(): Boolean {
        return success
    }

    val executionTime: LocalDateTime = LocalDateTime.now();
}