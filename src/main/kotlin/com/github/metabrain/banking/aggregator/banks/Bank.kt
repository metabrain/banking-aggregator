package com.github.metabrain.banking.aggregator.banks

import com.github.metabrain.banking.aggregator.Txn
import java.math.BigDecimal

interface Bank {

    fun txns(): List<Txn>

    fun balance() = txns().balance()
    fun totalSpent() = txns().spent()
    fun totalEarned() = txns().earned()

}


fun List<Txn>.balance() = this
        .fold(BigDecimal.ZERO) { acc: BigDecimal, txn: Txn ->
            acc.plus(txn.amount)
        }

fun List<Txn>.spent() = this
        .fold(BigDecimal.ZERO) { acc: BigDecimal, txn: Txn ->
            acc.plus(if (txn.amount.signum() == -1) txn.amount else BigDecimal.ZERO)
        }.abs()

fun List<Txn>.earned() = this
        .fold(BigDecimal.ZERO) { acc: BigDecimal, txn: Txn ->
            acc.plus(if (txn.amount.signum() == 1) txn.amount else BigDecimal.ZERO)
        }