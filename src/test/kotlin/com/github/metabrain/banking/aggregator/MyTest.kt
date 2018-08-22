package com.github.metabrain.banking.aggregator

import com.github.metabrain.banking.aggregator.main
import org.junit.Ignore
import org.junit.Test

class MyTest {

    @Test
//    @Ignore
    fun loadMyBanks() {
        main(arrayOf(
                "--barclays", "../BARCLAYS from april 2014 to 15 august 2018.csv",
                "--monzo", "../MonzoDataExport_Alltime_2018-08-15_224852.csv"
        ))
    }

    @Test
//    @Ignore
    fun monzoOnly() {
        main(arrayOf(
                "--monzo", "../MonzoDataExport_Alltime_2018-08-15_224852.csv"
        ))
    }

    @Test
//    @Ignore
    fun barclaysOnly() {
        main(arrayOf(
                "--barclays", "../BARCLAYS from april 2014 to 15 august 2018.csv"
        ))
    }

    @Test
//    @Ignore
    fun monzoJulyOnly() {
        main(arrayOf(
                "--monzo", "../MonzoDataExport_July.csv"
        ))
    }

    @Test
//    @Ignore
    fun monzoPrepaidOnly() {
        main(arrayOf(
//                "--barclays", "../BARCLAYS from april 2014 to 15 august 2018.csv",
                "--monzo-prepaid", "../monzo-prepaid.csv"
        ))
    }

}
