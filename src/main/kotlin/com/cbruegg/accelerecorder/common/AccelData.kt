package com.cbruegg.accelerecorder.common

data class AccelData(val x: Long,
                     val y: Long,
                     val z: Long,
                     val didVibrate: Boolean,
                     val timestamp: Long,
                     val source: Source,
                     /**
                      *  diff to add to the local time to get actual time of this value
                      */
                     val timeDiff: Int = 0) {

    enum class Source {
        LOCAL, PEBBLE, BAND
    }

    fun toCsvRow(): String = "$x,$y,$z,$didVibrate,$timestamp,$source,$timeDiff"

    companion object {
        fun csvHeader() = "x,y,z,didVibrate,timestamp,source,timediff"

        fun parseRows(rows: String) = rows
                .split("\n")
                .asSequence()
                .drop(1)
                .filter { it.trim().isNotEmpty() }
                .map { parseRow(it) }

        fun parseRow(row: String): AccelData {
            val data = row.split(",")
            return AccelData(
                    data[0].toLong(),
                    data[1].toLong(),
                    data[2].toLong(),
                    data[3].toBoolean(),
                    data[4].toLong(),
                    Source.valueOf(data[5]),
                    data[6].toInt()
            )
        }
    }
}