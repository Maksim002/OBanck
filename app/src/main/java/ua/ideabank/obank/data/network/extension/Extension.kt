package ua.ideabank.obank.data.network.extension

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

internal inline fun <reified T> T.toJson(gson: Gson): JsonObject {
	return gson.toJsonTree(this) as JsonObject
}

fun JsonElement?.asStringOrEmpty(): String {
	return if (this == null || this.isJsonNull) ""
	else this.asString
}

fun JsonObject.getMemberStringOrEmpty(member: String): String {
	return when {
		!has(member) -> return ""
		else -> get(member).asStringOrEmpty()
	}
}

inline fun <reified T> deserializeType() = object : TypeToken<T>() {}.type
