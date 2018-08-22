package com.github.metabrain.banking.aggregator

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.math.BigDecimal

/*sealed*/ open class Txn(
        val id: String? = null,
        val emoji: String? = null,
        val description: String,
        val notes: String? = null,
        val address: String? = null,
        val category: String? = null,
        val date: DateTime,
        val amount: BigDecimal, // local
        val currency: String, // local
        val foreign_amount: BigDecimal = amount,
        val foreign_currency: String = currency
)