package com.fullcycle.msaccountbalance.model

import jakarta.persistence.*

@Entity
@Table(name = "balance")
data class AccountBalance(

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    val id: Long? = 0L,

    @Column(name = "account_id")
    val accountId: String,

    val value: Double = 0.00
)