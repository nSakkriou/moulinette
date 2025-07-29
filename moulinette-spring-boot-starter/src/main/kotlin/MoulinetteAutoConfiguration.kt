package com.nathansakkriou

import com.nathansakkriou.domain.MoulinetteProcessor
import com.nathansakkriou.domain.ReplayabilityBehaviour
import com.nathansakkriou.domain.api.MoulinetteApi
import com.nathansakkriou.domain.persist.Persist
import com.nathansakkriou.domain.persist.PersistInMemory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

@AutoConfiguration
@ConditionalOnClass(MoulinetteApi::class)
@EnableConfigurationProperties(MoulinetteProperties::class)
open class MoulinetteAutoConfiguration(
    private val properties: MoulinetteProperties,
){

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(PersistCsv::class)
    open fun persist(): Persist {
        return PersistCsv(PersistCsvConfig(filePath = properties.csvPersistFilePath))
    }

    @Bean
    @ConditionalOnMissingBean
    open fun persistStub(): Persist {
        return PersistInMemory()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun moulinetteApi(): MoulinetteProcessor {
        return MoulinetteApi.create()
            .with("test", "me") { println("########### ${properties.message} ###########3") }
            .withReplayabilityBehaviour(ReplayabilityBehaviour.REPLAYABLE)
            .build()
    }

    @Bean
    @ConditionalOnMissingBean
    open fun moulinetteApiCommandLineRunner(processor: MoulinetteProcessor): CommandLineRunner {
        return CommandLineRunner {
            processor.run()
        }
    }
}