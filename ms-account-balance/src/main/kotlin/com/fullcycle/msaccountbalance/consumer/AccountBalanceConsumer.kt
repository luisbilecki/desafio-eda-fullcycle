package com.fullcycle.msaccountbalance.consumer

import com.fullcycle.msaccountbalance.dto.AccountBalanceUpdatedMessage
import com.fullcycle.msaccountbalance.service.AccountBalanceService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class AccountBalanceConsumer(val service: AccountBalanceService) {

    @KafkaListener(topics = ["balances"], groupId = "ms-account-balance", containerFactory = "accountBalanceUpdatedContainerFactory")
    fun listenBalances(message: AccountBalanceUpdatedMessage) {
        service.update(message.accountIdFrom, message.balanceAccountIdFrom)
        service.update(message.accountIdTo, message.balanceAccountIdTo)
    }
}