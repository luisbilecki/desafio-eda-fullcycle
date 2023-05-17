package com.fullcycle.msaccountbalance.controller

import com.fullcycle.msaccountbalance.model.AccountBalance
import com.fullcycle.msaccountbalance.service.AccountBalanceService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("balances")
class AccountBalanceController(private val service: AccountBalanceService) {

    @GetMapping("/{account_id}")
    fun getByAccountId(@PathVariable("account_id") accountId: String): ResponseEntity<AccountBalance?> {
        val balance = service.get(accountId)
            ?: return ResponseEntity(
                null,
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity.ok(balance)
    }
}