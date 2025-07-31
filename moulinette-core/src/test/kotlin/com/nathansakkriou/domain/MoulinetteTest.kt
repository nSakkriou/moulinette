package com.nathansakkriou.domain

import com.nathansakkriou.domain.api.MoulinetteApi
import com.nathansakkriou.domain.api.MoulinetteInBuild
import com.nathansakkriou.domain.persist.PersistInMemory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class MoulinetteTest {
    @Test
    fun `should offer possibility to create moulinette and execute it in declaration order`() {
        // Given
        val array = mutableListOf<String>();

        val moulinetteProcessor = MoulinetteApi.create()
            .withPersistance(PersistInMemory())
            .with(name = "name", author = "fef", action = {array.add("name")})
            .andWith(name = "name2", author = "fee", action = { array.add("name2") })
            .build()

        moulinetteProcessor.run();

        Assertions.assertEquals(array.size, 2);
        Assertions.assertEquals(array[0], "name");
        Assertions.assertEquals(array[1], "name2");
    }

    @Test
    fun `should throw error when two moulinette have the same name`() {
        val moulinetteBuild = MoulinetteApi.create()
            .withPersistance(PersistInMemory())
            .with(name = "name", author = "efe", action = { println("name") })
            .withReplayabilityBehaviour(ReplayabilityBehaviour.RUN_ONCE)
            .andWith(name = "name", author = "fef", action = { println("name2") })
            .withReplayabilityBehaviour(ReplayabilityBehaviour.REPLAYABLE)

        org.junit.jupiter.api.assertThrows<IllegalArgumentException>("Cant have same name") { moulinetteBuild.build() }
    }
}