package com.fullcycle.msaccountbalance.repository

import com.fullcycle.msaccountbalance.model.AccountBalance
import org.springframework.data.jpa.repository.JpaRepository

interface AccountBalanceRepository : JpaRepository<AccountBalance, Long> {
    fun findByAccountId(accountId: String): AccountBalance?
}