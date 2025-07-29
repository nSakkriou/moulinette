package com.nathansakkriou.domain.persist

import com.nathansakkriou.domain.identification.MoulinetteIdentification

interface Persist {
    fun saveMoulinetteExecution(moulinetteExecution: MoulinetteExecution)
    fun isAlreadyExecuted(moulinetteIdentification: MoulinetteIdentification): Boolean
}