package com.nathansakkriou.domain;

import com.nathansakkriou.domain.identification.MoulinetteAuthor
import com.nathansakkriou.domain.identification.MoulinetteIdentification
import com.nathansakkriou.domain.identification.MoulinetteName
import com.nathansakkriou.domain.persist.MoulinetteExecution

class RunnableMoulinette(
    private val name: MoulinetteName,
    private val author: MoulinetteAuthor,
    private val action: MoulinetteAction,
    private val replayabilityBehaviour: ReplayabilityBehaviour,
    private val onFailBehaviour: OnFailBehaviour
) : Moulinette {

    override fun getOnFailBehaviour(): OnFailBehaviour {
        return onFailBehaviour
    }

    override fun getReplayBehaviour(): ReplayabilityBehaviour {
        return replayabilityBehaviour
    }

    override fun getIdentification(): MoulinetteIdentification {
        return MoulinetteIdentification(
            name = name,
            author = author,
        )
    }

    override fun execute(): MoulinetteExecution {
        try {
            action.run()
            return MoulinetteExecution(
                identification = getIdentification(),
                success = true
            )
        }
        catch (e: Exception) {
            return MoulinetteExecution(
                identification = getIdentification(),
                success = false,
            )
        }
    }
}
