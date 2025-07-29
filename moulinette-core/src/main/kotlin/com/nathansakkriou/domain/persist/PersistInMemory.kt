package com.nathansakkriou.domain.persist

import com.nathansakkriou.domain.identification.MoulinetteIdentification

class PersistInMemory : Persist {
    private val state = mutableListOf<MoulinetteExecution>()

    override fun saveMoulinetteExecution(moulinetteExecution: MoulinetteExecution) {
        state.add(moulinetteExecution)
    }

    override fun isAlreadyExecuted(moulinetteIdentification: MoulinetteIdentification): Boolean {
        return state.stream().map { it.getIdentification() }.toList().contains(moulinetteIdentification)
    }
}