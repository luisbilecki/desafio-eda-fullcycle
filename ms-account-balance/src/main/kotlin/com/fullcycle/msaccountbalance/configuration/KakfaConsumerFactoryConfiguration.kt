package com.fullcycle.msaccountbalance.configuration

import com.fullcycle.msaccountbalance.dto.AccountBalanceUpdatedMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer


@Configuration
class KakfaConsumerFactoryConfiguration(
    @Value("\${spring.kafka.consumer.bootstrap-servers}")
    val bootstrapServerUrl: String
) {
    fun consumerConfig(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServerUrl
        props[ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS] = ErrorHandlingDeserializer::class.java
        props[ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS] = ErrorHandlingDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        props[JsonDeserializer.USE_TYPE_INFO_HEADERS] = false;
        props[JsonDeserializer.TRUSTED_PACKAGES] = "*";

        return props
    }

    @Bean
    fun accountBalanceUpdatedConsumerFactory() = DefaultKafkaConsumerFactory(
        consumerConfig(),
        StringDeserializer(),
        JsonDeserializer(
            AccountBalanceUpdatedMessage::class.java
        )
    )

    @Bean
    fun accountBalanceUpdatedContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, AccountBalanceUpdatedMessage>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, AccountBalanceUpdatedMessage> =
            ConcurrentKafkaListenerContainerFactory<String, AccountBalanceUpdatedMessage>()
        factory.consumerFactory = accountBalanceUpdatedConsumerFactory()
        return factory
    }
}