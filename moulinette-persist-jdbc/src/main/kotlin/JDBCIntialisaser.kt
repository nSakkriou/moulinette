package com.nathansakkriou

import com.nathansakkriou.domain.identification.MoulinetteIdentification
import com.nathansakkriou.domain.persist.MoulinetteExecution
import com.nathansakkriou.domain.persist.Persist
import java.sql.Connection
import java.sql.DriverManager

class JDBCPersist(
    private val config: JDBCConfig
) : Persist {

    init {
        getConnection().use { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS moulinette_execution (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        author VARCHAR(255) NOT NULL,
                        execution_time TIMESTAMP NOT NULL,
                        success BOOLEAN NOT NULL
                    )
                    """.trimIndent()
                )
                println("✅ Table moulinette_execution créée ou déjà existante.")
            }
        }
    }

    override fun saveMoulinetteExecution(moulinetteExecution: MoulinetteExecution) {
        getConnection().use { conn ->
            conn.prepareStatement(
                """
                INSERT INTO moulinette_execution (name, author, execution_time, success)
                VALUES (?, ?, ?, ?)
                """.trimIndent()
            ).use { ps ->
                ps.setString(1, moulinetteExecution.getIdentification().name.getValue())
                ps.setString(2, moulinetteExecution.getIdentification().author.getValue())
                ps.setTimestamp(3, java.sql.Timestamp.valueOf(moulinetteExecution.executionTime))
                ps.setBoolean(4, moulinetteExecution.isSuccess())
                ps.executeUpdate()
            }
        }
    }

    override fun isAlreadyExecuted(moulinetteIdentification: MoulinetteIdentification): Boolean {
        getConnection().use { conn ->
            conn.prepareStatement(
                """
                SELECT COUNT(*) FROM moulinette_execution
                WHERE name = ? AND author = ?
                """.trimIndent()
            ).use { ps ->
                ps.setString(1, moulinetteIdentification.name.getValue())
                ps.setString(2, moulinetteIdentification.author.getValue())
                ps.executeQuery().use { rs ->
                    rs.next()
                    return rs.getInt(1) > 0
                }
            }
        }
    }

    private fun getConnection(): Connection {
        return DriverManager.getConnection(config.url, config.user, config.password)
    }
}
