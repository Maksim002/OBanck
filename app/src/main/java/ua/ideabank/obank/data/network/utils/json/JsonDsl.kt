package ua.ideabank.obank.data.network.utils.json

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

internal typealias JsonObjectBody = JsonObjectBuilder.() -> Unit
internal typealias JsonArrayBody = JsonArrayBuilder.() -> Unit

/**
 * Creates a [JsonArray], calls the provided function on it, and returns it
 */
internal fun jsonObject(init: JsonObjectBuilder.() -> Unit): JsonObject {
	val builder = JsonObjectBuilder()
	builder.init()
	return builder.build()
}

/**
 * Returns a [JsonArray] with the elements provided.
 *
 * Items provided must either be [Boolean], [Char], [String], [Number], or [JsonElement]
 */
internal fun jsonArrayOf(vararg item: Any?): JsonArray {
	val jsonArray = JsonArray()
	item.forEach {
		when (it) {
			is Boolean -> jsonArray.add(it)
			is Char -> jsonArray.add(it)
			is String -> jsonArray.add(it)
			is Number -> jsonArray.add(it)
			is JsonElement -> jsonArray.add(it)
			null -> jsonArray.add(JsonNull.INSTANCE)
			else -> jsonArray.add(it.toString())
		}
	}
	return jsonArray
}

/**
 * Converts a [List] to a [JsonArray]
 */
internal fun List<*>.toJsonArray(): JsonArray {
	val jsonArray = JsonArray()
	this.forEach {
		when (it) {
			is Boolean -> jsonArray.add(it)
			is Char -> jsonArray.add(it)
			is String -> jsonArray.add(it)
			is Number -> jsonArray.add(it)
			is JsonElement -> jsonArray.add(it)
			null -> jsonArray.add(JsonNull.INSTANCE)
			else -> jsonArray.add(it.toString())
		}
	}
	return jsonArray
}

/**
 * Alternative syntax for [List].[toJsonArray]. Converts the [List] into a [JsonArray].
 */
internal fun jsonArrayOf(list: List<*>) = list.toJsonArray()

/**
 * Alternative syntax for [List].[toJsonArray]. Converts the [List] into a [JsonArray].
 */
internal fun <T> jsonArrayOf(list: List<T>, block: JsonObjectBuilder.(item: T) -> Unit): JsonArray {
	val jsonArray = JsonArray()
	list.map {
		JsonObjectBuilder()
			.run {
				block(it)
				build()
			}
			.apply(jsonArray::add)
	}

	return jsonArray
}

/**
 * Converts a [HashMap] to a [JsonObject]
 */
internal fun <X, Y> HashMap<X, Y>.toJsonObject(): JsonObject {
	return jsonObject {
		this@toJsonObject.forEach { it.key.toString() to it.value }
	}
}