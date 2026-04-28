package br.com.ccortez.feature.taxi_travel_options.ui

import kotlinx.serialization.json.Json
import java.io.InputStream

object JsonProvider {

    inline fun <reified U> objectFromJsonFileWithType(filePath: String): U {
        val jsonString = readResourceFile(filePath)
        return if (jsonString!= null) {
            Json.decodeFromString<U>(jsonString)
        } else {
            throw IllegalArgumentException("Error: Resource not found or invalid JSON.")
        }
    }

    fun readResourceFile(fileName: String): String? {
        val inputStream: InputStream? = this::class.java.getResourceAsStream(fileName)
        return inputStream?.bufferedReader()?.use { it.readText() }
    }
}