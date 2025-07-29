package com.nathansakkriou.domain.api;

import com.nathansakkriou.domain.*
import com.nathansakkriou.domain.persist.Persist

interface MoulinetteApi {
    companion object {
        @JvmStatic
        fun create(): ProcessorConfig {
            return MoulinetteApiImpl()
        }
    }
}

interface ProcessorConfig: CanChoicePersistanceImpl {
    fun with(name: String, author: String, action: MoulinetteAction): MoulinetteDeclaration
}

interface MoulinetteDeclaration : CanBeParameterize, CanBuild {
    fun andWith(name: String, author: String, action: MoulinetteAction): MoulinetteDeclaration
}

// ----

interface CanBuild {
    fun build(): MoulinetteProcessor
}

interface CanBeParameterize {
    fun withOnFailBehaviour(onFailBehaviour: OnFailBehaviour): MoulinetteDeclaration
    fun withReplayabilityBehaviour(replayabilityBehaviour: ReplayabilityBehaviour): MoulinetteDeclaration
}

interface CanChoicePersistanceImpl {
    fun withPersistance(persist: Persist): ProcessorConfig
}



