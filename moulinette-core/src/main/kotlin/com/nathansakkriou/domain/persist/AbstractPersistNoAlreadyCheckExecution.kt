package com.nathansakkriou.domain.persist

import com.nathansakkriou.domain.identification.MoulinetteIdentification

/**
 * Used to fill persist param in MoulinetteProcessor,
 * when all moulinette are on replayable
 */
abstract class AbstractPersistNoAlreadyCheckExecution: Persist {
    override fun isAlreadyExecuted(moulinetteIdentification: MoulinetteIdentification): Boolean {
        return false
    }
}