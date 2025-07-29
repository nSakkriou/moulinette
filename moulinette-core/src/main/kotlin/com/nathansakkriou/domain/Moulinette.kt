package com.nathansakkriou.domain

import com.nathansakkriou.domain.identification.MoulinetteIdentification
import com.nathansakkriou.domain.persist.MoulinetteExecution

interface Moulinette {
    fun getOnFailBehaviour(): OnFailBehaviour
    fun getReplayBehaviour(): ReplayabilityBehaviour
    fun getIdentification(): MoulinetteIdentification
    fun execute(): MoulinetteExecution

}