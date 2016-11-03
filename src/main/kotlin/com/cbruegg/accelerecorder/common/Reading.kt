package com.cbruegg.accelerecorder.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

private val mapper = jacksonObjectMapper()

data class Reading(val values: Map<String, Double>,
                   val didVibrate: Boolean,
                   val timestamp: Long,
                   val source: Source,
                   val sensor: Sensor,
                   /**
                    *  diff to add to the local time to get actual time of this value
                    */
                   val timeDiff: Int = 0) {

    enum class Source {
        LOCAL, PEBBLE, BAND
    }

    enum class Sensor {
        Accelerometer, Gyroscope
    }

    fun serialize(): String = mapper.writeValueAsString(this)

    companion object {
        fun deserialize(str: String): Reading = mapper.readValue(str)
    }

}