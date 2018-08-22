package com.github.metabrain.banking.aggregator.banks

import com.github.metabrain.banking.aggregator.CsvFile
import com.github.metabrain.banking.aggregator.Txn
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.math.BigDecimal

class Barclays(val txns: List<BarclaysTxn>): Bank {
    override fun txns() = txns

    companion object {
        fun parseTxnFromCsvs(barclaysCsv: CsvFile): Barclays {

            println("barclays: ${barclaysCsv.headers}")

            val barclaysTxns: List<BarclaysTxn> = barclaysCsv.rows
                    .map { row ->
                        row.mapIndexed { idx, col ->
                            // NOTE: Basically, they use commas in some "Memos" without escaping it, which causes issues...
                            Pair(barclaysCsv.headers[Math.min(idx, barclaysCsv.headers.size - 1)].toLowerCase(), col)
                        }.groupBy({
                            it.first
                        }, {
                            it.second
                        })
                                .map { (k, v) ->
                                    // And finally we flatten repeated "Memos"
                                    Pair(k, v.joinToString(","))
                                }.toMap()
                    }.map { properties ->
                        BarclaysTxn.from(properties)
                    }
                    .reversed()
            //            .filter {
            // FIXME REMOVE
            //                txn -> txn.date.isAfter(DateTime().withDate(2018,6,6)) and txn.date.isBefore(DateTime().withDate(2018,7,6))
            //            }
            //    barclaysTxns.forEach {
            //        println(it)
            //    }
            println("num txn barclays: ${barclaysTxns.size}")
            println()
            println()
            println()
//        val barclaysTxns = if(barclays!=null) Barclays.parseTxnsFromCsv(barclays) else emptyList()


            return Barclays(barclaysTxns)
        }
    }
}


data class BarclaysTxn(
        val number: String?,
        val date_: DateTime,
        val account: String,
        val amount_: BigDecimal,
        val subcategory: String,
        val memo: String
): Txn(
        id = number,
        description = memo,
//        address = address_,
        category = subcategory, // TODO need proper mapping here? because monzo categories and barclays categories mean different things
        date = date_,
        amount = amount_,
        currency = "GBP" // all in GBP..?
//        local_amount = local_amount_,
//        local_currency = local_currency_,
) {

    companion object {

        fun from(properties: Map<String, Any?>): BarclaysTxn {
            return BarclaysTxn(
                    number = properties["number"].toString(),
                    date_ = parseBarclaysTime(properties["date"].toString()),
                    account = properties["account"].toString(),
                    amount_ = properties["amount"].toString().toBigDecimal(),
                    subcategory = properties["subcategory"].toString(),
                    memo = properties["memo"].toString()
            )
        }

        fun parseBarclaysTime(str: String): DateTime {
            //15/08/2018
            return DateTime.parse(str, DateTimeFormat.forPattern("dd/MM/yyyy"))
        }
    }
}
