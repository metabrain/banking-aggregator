package com.github.metabrain.banking.aggregator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.metabrain.banking.aggregator.banks.*
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) = BankingCsvAggregator()
        .main(args)

class BankingCsvAggregator : CliktCommand() {
    val barclaysFile: String? by option(
            names = *arrayOf("--barclays"),
            help = "For Barclays CSV path exported from their website"

    )

    val monzoFile: String? by option(
            names = *arrayOf("--monzo"),
            help = "For Monzo CSV path exported from their app"
    )

    val monzoPrepaidFile: String? by option(
            names = *arrayOf("--monzo-prepaid"),
            help = "For Monzo prepaid (less info) CSV path exported from their app"
    )

    override fun run() {

        val monzo: Monzo? = if(monzoFile!=null || monzoPrepaidFile!=null) {
            Monzo.parseTxnFromCsvs(
                    if (monzoFile != null) CsvParser.parseLines(Files.readAllLines(Paths.get(monzoFile), Charsets.UTF_8)) else CsvFile.EMPTY,
                    if (monzoPrepaidFile != null) CsvParser.parseLines(Files.readAllLines(Paths.get(monzoPrepaidFile), Charsets.UTF_8)) else CsvFile.EMPTY
            )
        } else null

        val barclays: Barclays? = if(barclaysFile!=null) {
            Barclays.parseTxnFromCsvs(
                    CsvParser.parseLines(Files.readAllLines(Paths.get(barclaysFile), Charsets.ISO_8859_1))
            )
        } else null

        val txns: List<Txn> =
                emptyList<Txn>() +
                        (barclays?.txns() ?: emptyList()) +
                        (monzo?.txns() ?: emptyList())
        //                    .sortedBy { it.amount.toDouble().absoluteValue }
        //                    .filter { txn -> txn.amount.toDouble()>0.0 }

        txns.forEach { println(it as Txn) }

        println()
        println()
        println()
        println("  TOTAL EARNED: ${txns.earned()}")
        println("- TOTAL SPENT: ${txns.spent()}")
        println("  ----------------")
        println("  BALANCE: ${txns.balance()}")

    }
}
