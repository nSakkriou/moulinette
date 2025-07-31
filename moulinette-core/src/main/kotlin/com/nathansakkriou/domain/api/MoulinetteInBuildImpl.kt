package com.nathansakkriou.domain.api

import com.nathansakkriou.domain.*
import com.nathansakkriou.domain.identification.MoulinetteAuthor
import com.nathansakkriou.domain.identification.MoulinetteName

interface MoulinetteInBuild {
    fun getName(): String
    fun getAuthor(): String
    fun getAction(): MoulinetteAction
    fun getOnFailBehavior(): OnFailBehaviour
    fun getReplayabilityBehaviour(): ReplayabilityBehaviour
    fun setOnFailBehaviour(failBehaviour: OnFailBehaviour)
    fun setReplayabilityBehaviour(replayabilityBehaviour: ReplayabilityBehaviour)
    fun toMoulinette(): Moulinette
}

data class MoulinetteInBuildImpl(
    private val name: String,
    private val author: String,
    private val action: MoulinetteAction,
    private var onFailBehaviour: OnFailBehaviour = OnFailBehaviour.STOP_ON_FAIL,
    private var replayabilityBehaviour: ReplayabilityBehaviour = ReplayabilityBehaviour.RUN_ONCE
) : MoulinetteInBuild{
    override fun toMoulinette(): Moulinette {
        return RunnableMoulinette(
            name = MoulinetteName(name),
            author = MoulinetteAuthor(author),
            action = action,
            onFailBehaviour = onFailBehaviour,
            replayabilityBehaviour = replayabilityBehaviour
        )
    }

    override fun getName(): String {
        return name
    }

    override fun getAuthor(): String {
        return author
    }

    override fun getAction(): MoulinetteAction {
        return action
    }

    override fun getOnFailBehavior(): OnFailBehaviour {
        return onFailBehaviour
    }

    override fun getReplayabilityBehaviour(): ReplayabilityBehaviour {
        return replayabilityBehaviour
    }

    override fun setOnFailBehaviour(failBehaviour: OnFailBehaviour) {
        this.onFailBehaviour = failBehaviour
    }

    override fun setReplayabilityBehaviour(replayabilityBehaviour: ReplayabilityBehaviour) {
        this.replayabilityBehaviour = replayabilityBehaviour
    }
}