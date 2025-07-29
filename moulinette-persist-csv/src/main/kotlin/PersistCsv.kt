package com.nathansakkriou

import com.nathansakkriou.domain.identification.MoulinetteIdentification
import com.nathansakkriou.domain.persist.MoulinetteExecution
import com.nathansakkriou.domain.persist.Persist

import java.io.File
import java.io.FileWriter
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class PersistCsv(private val config: PersistCsvConfig) : Persist {

    private val file = File(config.filePath)

    init {
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    override fun saveMoulinetteExecution(moulinetteExecution: MoulinetteExecution) {
        try {
            FileWriter(file, true).use { writer ->
                val line = buildString {
                    append(moulinetteExecution.getIdentification().name.getValue())
                    append(";")
                    append(moulinetteExecution.getIdentification().author.getValue())
                    append(";")
                    append(moulinetteExecution.executionTime)
                    append(";")
                    append(moulinetteExecution.isSuccess())
                }
                writer.write(line + "\n")
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to write execution to CSV", e)
        }
    }

    override fun isAlreadyExecuted(moulinetteIdentification: MoulinetteIdentification): Boolean {
        if (!file.exists()) return false

        BufferedReader(FileReader(file)).useLines { lines ->
            return lines.any { line ->
                val parts = line.split(";")
                if (parts.size >= 2) {
                    val name = parts[0]
                    val author = parts[1]
                    name == moulinetteIdentification.name.getValue() &&
                            author == moulinetteIdentification.author.getValue()
                } else {
                    false
                }
            }
        }
    }
}
