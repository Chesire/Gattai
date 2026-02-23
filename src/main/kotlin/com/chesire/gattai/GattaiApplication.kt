package com.chesire.gattai

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GattaiApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<GattaiApplication>(*args)
}
