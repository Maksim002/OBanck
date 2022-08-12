package ua.ideabank.obank.data.network.utils.json

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 * Wrapper class for a [JsonArray] which provides the DSL syntax
 */
internal class JsonArrayBuilder internal constructor() {

	private val array = JsonArray()

	operator fun JsonObject.unaryPlus() {
		array.add(this)
	}

	operator fun List<JsonObject>.unaryPlus() {
		forEach(array::add)
	}

	operator fun JsonArray.unaryPlus() {
		array.addAll(this)
	}

	inline fun <T> List<T>.toJsonObjectList(block: JsonObjectBuilder.(item: T) -> Unit): List<JsonObject> = map {
		JsonObjectBuilder().run {
			block(it)
			build()
		}
	}

	/**
	 * Return the completed [JsonObject]
	 */
	fun build(): JsonArray {
		return array
	}

}