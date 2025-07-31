package com.nathansakkriou.domain.api

import com.nathansakkriou.domain.MoulinetteAction
import com.nathansakkriou.domain.MoulinetteProcessor
import com.nathansakkriou.domain.OnFailBehaviour
import com.nathansakkriou.domain.ReplayabilityBehaviour
import com.nathansakkriou.domain.persist.Persist
import com.nathansakkriou.domain.persist.PersistNotAlreadyCheckExecutionInMemory
import java.util.*
import java.util.stream.Collectors

class MoulinetteApiImpl : ProcessorConfig, MoulinetteDeclaration {

    private val moulinettesInBuild = mutableListOf<MoulinetteInBuild>()
    private var persistImpl: Optional<Persist> = Optional.empty();

    override fun with(name: String, author: String, action: MoulinetteAction): MoulinetteDeclaration {
        val newMoulinetteInBuild = MoulinetteInBuildImpl(name, author, action, OnFailBehaviour.STOP_ON_FAIL, ReplayabilityBehaviour.RUN_ONCE)
        moulinettesInBuild.add(newMoulinetteInBuild)
        return this;
    }

    override fun andWith(name: String, author: String, action: MoulinetteAction): MoulinetteDeclaration {
        return with(name, author, action)
    }

    override fun with(vararg moulinetteInBuild: MoulinetteInBuild): MoulinetteDeclaration {
        moulinettesInBuild.addAll(moulinetteInBuild)
        return this;
    }

    override fun andWith(vararg moulinetteInBuild: MoulinetteInBuild): MoulinetteDeclaration {
        return with(*moulinetteInBuild)
    }

    override fun withPersistance(persist: Persist): ProcessorConfig {
        persistImpl = Optional.of(persist);
        return this
    }

    override fun withOnFailBehaviour(onFailBehaviour: OnFailBehaviour): MoulinetteDeclaration {
        val moulinetteInBuild = moulinettesInBuild[moulinettesInBuild.lastIndex]
        moulinetteInBuild.setOnFailBehaviour(onFailBehaviour)
        return this
    }

    override fun withReplayabilityBehaviour(replayabilityBehaviour: ReplayabilityBehaviour): MoulinetteDeclaration {
        val moulinetteInBuild = moulinettesInBuild[moulinettesInBuild.lastIndex]
        moulinetteInBuild.setReplayabilityBehaviour(replayabilityBehaviour)
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
        moulinettesInBuild.stream().map { it.getReplayabilityBehaviour() }
            .filter { it == ReplayabilityBehaviour.REPLAYABLE }.toList().size == moulinettesInBuild.size

    private fun atLeastTwoMoulinetteHaveTheSameName() = moulinettesInBuild.stream().map { it.getName() }.collect(Collectors.toSet()).size == moulinettesInBuild.size

}

