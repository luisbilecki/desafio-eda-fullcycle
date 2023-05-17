package com.fullcycle.msaccountbalance.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AccountBalanceUpdatedMessage(
    @JsonProperty("account_id_from")
    val accountIdFrom: String,
    @JsonProperty("account_id_to")
    val accountIdTo: String,
    @JsonProperty("balance_account_id_from")
    val balanceAccountIdFrom: Float,
    @JsonProperty("balance_account_id_to")
    val balanceAccountIdTo: Float,
)
