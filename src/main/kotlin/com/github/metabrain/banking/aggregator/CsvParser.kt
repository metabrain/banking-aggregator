package com.github.metabrain.banking.aggregator

/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object CsvParser {
    fun parseLine(line: String, separator: Char = ','): List<String> {
        val result = mutableListOf<String>()
        val builder = StringBuilder()
        var quotes = 0
        for (ch in line) {
            when {
                ch == '\"' -> {
                    quotes++
                    builder.append(ch)
                }
                (ch == '\n') || (ch == '\r') -> {
                }
                (ch == separator) && (quotes % 2 == 0) -> {
                    if(quotes==0) {
                        result.add(builder.toString())
                    } else {
                        // trim before
                        result.add(builder.substring(1, builder.length-1).toString())
                    }
                    builder.setLength(0)
                    quotes = 0
                }
                else -> builder.append(ch)
            }
        }
        if(builder.isNotEmpty()) {
            if(quotes==0) {
                result.add(builder.toString())
            } else {
                // trim before
                result.add(builder.substring(1, builder.length-1).toString())
            }
        }
        return result
    }

    fun parseLines(lines: List<String>): CsvFile {
        return CsvFile(
                headers = parseLine(lines.first()),
                rows = lines.slice(1 until lines.size).map { line -> parseLine(line) }
        )
    }

}

data class CsvFile(
        val headers: List<String>,
        val rows: List<List<String>>
) {
    companion object {
        val EMPTY = CsvFile(emptyList(), emptyList())
    }
}