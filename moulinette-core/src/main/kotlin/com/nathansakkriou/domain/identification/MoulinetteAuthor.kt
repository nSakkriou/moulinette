package com.nathansakkriou.domain.identification

class MoulinetteAuthor(private val value: String) {

    init {
        require(value.isNotEmpty())
    }

    fun getValue(): String {
        return value
    }

    override fun toString(): String {
        return value
    }
}