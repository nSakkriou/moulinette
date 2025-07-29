package com.nathansakkriou.domain;

import com.nathansakkriou.domain.persist.Persist

class MoulinetteProcessor(
    private val moulinettes: List<Moulinette>,
    private val persist: Persist
) {

    fun run() {
        moulinettes.forEach {
            when(it.getReplayBehaviour()) {
                ReplayabilityBehaviour.RUN_ONCE -> {
                    if(persist.isAlreadyExecuted(it.getIdentification()))
                        return

                    persist.saveMoulinetteExecution(it.execute())
                };
                ReplayabilityBehaviour.REPLAYABLE -> {
                    persist.saveMoulinetteExecution(it.execute())
                }
            }
        }
    }
}
