package io.naen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NaenioApplication

fun main(args: Array<String>) {
    runApplication<NaenioApplication>(*args)
}
