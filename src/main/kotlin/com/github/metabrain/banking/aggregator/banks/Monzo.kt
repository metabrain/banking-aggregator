package com.github.metabrain.banking.aggregator.banks

import com.github.metabrain.banking.aggregator.CsvFile
import com.github.metabrain.banking.aggregator.Txn
import org.joda.time.DateTime
import java.math.BigDecimal

data class Monzo(val txns: List<MonzoTxn>, val prepaidTxns: List<MonzoPrepaidTxn>) : Bank {
    override fun txns() = txns + prepaidTxns

    companion object {
        fun parseTxnFromCsvs(monzoCsv: CsvFile?, monzoPrepaidCsv: CsvFile?): Monzo {

            val monzoTxns: List<MonzoTxn> = if(monzoCsv!=null) {
                println("monzo: ${monzoCsv.headers}")
                monzoCsv.rows
                        .map { row ->
                            row.mapIndexed { idx, col ->
                                Pair(monzoCsv.headers[idx].toLowerCase(), col)
                            }.toMap()
                        }
                        .map { properties ->
                            MonzoTxn.from(properties)
                        }
                        .apply {
                            println("num monzo txns: ${this.size}")
                        }
                //            .filter {
                //                // FIXME REMOVE
                //                txn -> txn.date.isAfter(DateTime().withDate(2018,7, 1)) and txn.date.isBefore(DateTime().withDate(2018,8,1))
                //            }
                //    monzoTxns.forEach {
                //        println(it)
                //    }
            } else emptyList()

            val monzoPrepaidTxns: List<MonzoPrepaidTxn> = if(monzoPrepaidCsv!=null) {
                println("monzo prepaid: ${monzoPrepaidCsv.headers}")
                monzoPrepaidCsv.rows
                        .map { row ->
                            row.mapIndexed { idx, col ->
                                Pair(monzoPrepaidCsv.headers[idx].toLowerCase(), col)
                            }.toMap()
                        }
                        .map { properties ->
                            println(properties)
                            MonzoPrepaidTxn.from(properties)
                        }
                        .apply {
                            println("num monzo prepaid txns: ${this.size}")
                        }
                //            .filter {
                //                // FIXME REMOVE
                //                txn -> txn.date.isAfter(DateTime().withDate(2018,7, 1)) and txn.date.isBefore(DateTime().withDate(2018,8,1))
                //            }
                //    monzoTxns.forEach {
                //        println(it)
                //    }
            } else listOf()

            return Monzo(monzoTxns, monzoPrepaidTxns)
        }
    }
}


data class MonzoTxn(
        val id_: String,
        val created: DateTime,
        val amount_: BigDecimal,
        val currency_: String,
        val local_amount_: BigDecimal,
        val local_currency_: String,
        val category_: String,
        val emoji_: String,
        val description_: String,
        val address_: String,
        val notes_: String
): Txn(
        id = id_,
        emoji = emoji_,
        description = description_,
        notes = notes_,
        address = address_,
        category = category_,
        date = created,
        amount = amount_,
        currency = currency_,
        foreign_amount = local_amount_,
        foreign_currency = local_currency_
) {

    companion object {

        fun from(properties: Map<String, Any?>): MonzoTxn {
            return MonzoTxn(
                    id_ = properties["id"].toString(),
                    created = DateTime.parse(properties["created"].toString()),
                    amount_ = properties["amount"].toString().toBigDecimal(),
                    currency_ = properties["currency"].toString(),
                    local_amount_ = properties["local_amount"].toString().toBigDecimal(),
                    local_currency_ = properties["local_currency"].toString(),
                    category_ = properties["category"].toString(),
                    emoji_ = properties["emoji"].toString(),
                    description_ = properties["description"].toString(),
                    address_ = properties["address"].toString(),
                    notes_ = properties["notes"].toString()
            )
        }

    }

}

data class MonzoPrepaidTxn(
        val date_: DateTime,
        val description_: String,
        val amount_: BigDecimal,
        val currency_: String,
        val balance: BigDecimal
): Txn(
        description = description_,
        date = date_,
        amount = amount_,
        currency = currency_
) {

    companion object {
        fun parsePrepaidAmount(str: String) = str
                .replace("£", "")
                .replace("€", "")
                .toBigDecimal()

        fun parseCurrency(str: String) = when {
            str.contains("£") -> "GBP"
            str.contains("€") -> "EUR"
            else -> throw IllegalArgumentException("Can not parse currency of '$str'")
        }

        fun from(properties: Map<String, Any?>): MonzoPrepaidTxn {
            return MonzoPrepaidTxn(
                    date_ = DateTime.parse(properties["date"].toString()),
                    amount_ = parsePrepaidAmount(properties["amount"].toString()),//.toBigDecimal(),
                    description_ = properties["description"].toString(),
                    currency_ = parseCurrency(properties["amount"].toString()),
                    balance = parsePrepaidAmount(properties["balance"].toString())
            )
        }

    }

}