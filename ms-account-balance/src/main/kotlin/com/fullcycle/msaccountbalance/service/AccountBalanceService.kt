package com.fullcycle.msaccountbalance.service

import com.fullcycle.msaccountbalance.model.AccountBalance
import com.fullcycle.msaccountbalance.repository.AccountBalanceRepository
import org.springframework.stereotype.Service

@Service
class AccountBalanceService(private val repository: AccountBalanceRepository) {

    fun get(accountId: String) = repository.findByAccountId(accountId)

    fun update(accountId: String, newBalance: Float): AccountBalance {
        val balanceFound = repository.findByAccountId(accountId) ?: AccountBalance(accountId = accountId)
        val updatedBalance = balanceFound.copy(value = newBalance.toDouble())
        return repository.save(updatedBalance)
    }
}