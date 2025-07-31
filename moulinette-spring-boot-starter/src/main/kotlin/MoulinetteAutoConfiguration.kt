package com.nathansakkriou

import com.nathansakkriou.domain.MoulinetteProcessor
import com.nathansakkriou.domain.ReplayabilityBehaviour
import com.nathansakkriou.domain.api.MoulinetteApi
import com.nathansakkriou.domain.persist.Persist
import com.nathansakkriou.domain.persist.PersistInMemory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order

@AutoConfiguration
@ConditionalOnClass(MoulinetteApi::class)
@EnableConfigurationProperties(MoulinetteProperties::class)
open class MoulinetteAutoConfiguration(
    private val properties: MoulinetteProperties,
){

    @Value("\${spring.datasource.url}")
    private val jdbcUrl: String = ""

    @Value("\${spring.datasource.username}")
    private val username: String = ""

    @Value("\${spring.datasource.password}")
    private val password: String = ""

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(JDBCPersist::class)
    @ConditionalOnProperty(prefix = "spring.datasource", name = ["url", "username", "password"])
    open fun moulinettePersist(): Persist {
        return JDBCPersist(config = JDBCConfig(
            url = jdbcUrl,
            password = password,
            user = username,
        ))
    }

//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnClass(PersistCsv::class)
//    open fun persist(): Persist {
//        return PersistCsv(PersistCsvConfig(filePath = properties.csvPersistFilePath))
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    open fun persistStub(): Persist {
//        return PersistInMemory()
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    open fun moulinetteProcessor(): MoulinetteProcessor {
//        return MoulinetteApi.create()
//            .with("test", "me") { println("########### ${properties.message} ###########3") }
//            .withReplayabilityBehaviour(ReplayabilityBehaviour.REPLAYABLE)
//            .build()
//    }

    @Bean
    @ConditionalOnMissingBean
    @Order(value = Ordered.LOWEST_PRECEDENCE)
    @ConditionalOnProperty(prefix = MoulinetteConstants.PROPERTIES_PREFIX, name = [MoulinetteConstants.PROPERTIES_KEY_ENABLE], havingValue = GlobalConstants.TRUE_STRING, matchIfMissing = true)
    open fun moulinetteCommandLineRunner(processor: MoulinetteProcessor): CommandLineRunner {
        return CommandLineRunner {
            processor.run()
        }
    }
}