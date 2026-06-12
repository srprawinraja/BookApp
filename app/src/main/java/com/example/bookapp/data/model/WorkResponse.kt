package com.example.bookapp.data.model

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.lang.reflect.Type

data class WorkResponse(
    @SerializedName("description")
    val description: Any? = null
) {
    fun getDescriptionText(): String {
        return when (description) {
            is String -> description
            is Map<*, *> -> (description["value"] as? String) ?: ""
            else -> "No summary available."
        }
    }
}
