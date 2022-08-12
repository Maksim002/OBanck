package ua.ideabank.obank.data.network.utils.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.util.*

class DateSerializer : JsonSerializer<Date> {

	override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
		return JsonPrimitive(src?.time?.div(1_000) ?: 0L)
	}
}