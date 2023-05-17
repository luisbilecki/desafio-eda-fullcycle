package com.fullcycle.msaccountbalance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class MsAccountBalanceApplication

fun main(args: Array<String>) {
	runApplication<MsAccountBalanceApplication>(*args)
}
