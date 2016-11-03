package com.cbruegg.accelerecorder.common

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

private val mapper = jacksonObjectMapper()

data class Reading(
        /**
         * Set of keys is a function of source and sensor
         */
        val values: Map<String, Double>,
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

}

fun List<Reading>.writeAsJson(file: File) {
    mapper.writeValue(BufferedWriter(FileWriter(file)), this)
}

fun readReadings(json: String): List<Reading> = mapper.readValue(json)

fun List<Reading>.validateValueKeys() {
    val keysBySensorSource = mutableMapOf<Pair<Reading.Sensor, Reading.Source>, Set<String>>()
    for (reading in this) {
        val keys = keysBySensorSource.getOrPut(reading.sensor to reading.source) {
            reading.values.keys
        }
        if (reading.values.keys != keys) {
            throw IllegalArgumentException("$reading has invalid keys ${reading.values.keys}, expected $keys")
        }
    }
}