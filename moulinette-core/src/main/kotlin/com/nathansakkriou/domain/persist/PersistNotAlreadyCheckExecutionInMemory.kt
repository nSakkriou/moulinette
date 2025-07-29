package com.nathansakkriou.domain.persist

class PersistNotAlreadyCheckExecutionInMemory: AbstractPersistNoAlreadyCheckExecution() {
    private val state = mutableListOf<MoulinetteExecution>()

    override fun saveMoulinetteExecution(moulinetteExecution: MoulinetteExecution) {
        state.add(moulinetteExecution)
    }
}