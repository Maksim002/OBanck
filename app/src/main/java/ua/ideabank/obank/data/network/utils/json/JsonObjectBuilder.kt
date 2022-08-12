package ua.ideabank.obank.data.network.utils.json

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject

/**
 * Wrapper class for a [JsonObject] which provides the DSL syntax
 */
class JsonObjectBuilder internal constructor() {

	private val obj = JsonObject()

	/**
	 * Add a property with the given key
	 *
	 * Syntax: "key" to value
	 */
	internal infix fun String.to(value: Any?) {
		when (value) {
			is Boolean -> obj.addProperty(this, value)
			is Char -> obj.addProperty(this, value)
			is String -> obj.addProperty(this, value)
			is Number -> obj.addProperty(this, value)
			is JsonElement -> obj.add(this, value)
			null -> obj.add(this, JsonNull.INSTANCE)
			else -> obj.addProperty(this, value.toString())
		}
	}

	/**
	 * Return the completed [JsonObject]
	 */
	fun build(): JsonObject {
		return obj
	}

}