package com.nathansakkriou.domain.api

import com.nathansakkriou.domain.*
import com.nathansakkriou.domain.identification.MoulinetteAuthor
import com.nathansakkriou.domain.identification.MoulinetteName
import com.nathansakkriou.domain.persist.Persist
import com.nathansakkriou.domain.persist.PersistNotAlreadyCheckExecutionInMemory
import java.util.Optional
import java.util.stream.Collectors

class MoulinetteApiImpl : ProcessorConfig, MoulinetteDeclaration {

    private val moulinettesInBuild = mutableListOf<MoulinetteInBuild>()
    private var persistImpl: Optional<Persist> = Optional.empty();

    override fun with(name: String, author: String, action: MoulinetteAction): MoulinetteDeclaration {
        val newMoulinetteInBuild = MoulinetteInBuild(name, author, action, OnFailBehaviour.STOP_ON_FAIL, ReplayabilityBehaviour.RUN_ONCE)
        moulinettesInBuild.add(newMoulinetteInBuild)
        return this;
    }

    override fun withPersistance(persist: Persist): ProcessorConfig {
        persistImpl = Optional.of(persist);
        return this
    }

    override fun andWith(name: String, author: String, action: MoulinetteAction): MoulinetteDeclaration {
        return with(name, author, action)
    }

    override fun withOnFailBehaviour(onFailBehaviour: OnFailBehaviour): MoulinetteDeclaration {
        val moulinetteInBuild = moulinettesInBuild[moulinettesInBuild.lastIndex]
        moulinetteInBuild.onFailBehaviour = onFailBehaviour
        return this
    }

    override fun withReplayabilityBehaviour(replayabilityBehaviour: ReplayabilityBehaviour): MoulinetteDeclaration {
        val moulinetteInBuild = moulinettesInBuild[moulinettesInBuild.lastIndex]
        moulinetteInBuild.replayabilityBehaviour = replayabilityBehaviour
        return this
    }

    override fun build(): MoulinetteProcessor {
        require(atLeastTwoMoulinetteHaveTheSameName()) {"Cant have same name"}

        val persist: Persist
        if(allMoulinetteAreReplayable()) {
            persist = PersistNotAlreadyCheckExecutionInMemory()
        }
        else {
            require(persistImpl.isPresent) {"Have to select persistance impl"}
            persist = persistImpl.get()
        }

        return MoulinetteProcessor(moulinettesInBuild.stream().map { it.toMoulinette() }.toList(), persist)
    }

    private fun allMoulinetteAreReplayable() =
        moulinettesInBuild.stream().map { it.replayabilityBehaviour }
            .filter { it == ReplayabilityBehaviour.REPLAYABLE }.toList().size == moulinettesInBuild.size

    private fun atLeastTwoMoulinetteHaveTheSameName() = moulinettesInBuild.stream().map { it.name }.collect(Collectors.toSet()).size == moulinettesInBuild.size

}

data class MoulinetteInBuild(
    val name: String,
    val author: String,
    val action: MoulinetteAction,
    var onFailBehaviour: OnFailBehaviour,
    var replayabilityBehaviour: ReplayabilityBehaviour
) {
    fun toMoulinette(): Moulinette {
        return RunnableMoulinette(
            name = MoulinetteName(name),
            author = MoulinetteAuthor(author),
            action = action,
            onFailBehaviour = onFailBehaviour,
            replayabilityBehaviour = replayabilityBehaviour
        )
    }
}
