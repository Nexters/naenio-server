package io.naen

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.context.annotation.Bean

@EnableConfigServer
@SpringBootApplication
class NaenioApplication

fun main(args: Array<String>) {
    runApplication<NaenioApplication>(*args)
}

private val log: Logger = LoggerFactory.getLogger(NaenioApplication::class.java)

@Bean
fun testEnv(@Value("\${app.env}") env: String) {
    log.info(">>>> env=${env}")
}
