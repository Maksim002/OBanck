package ua.ideabank.obank.data.network.utils.factory.converter

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.ideabank.obank.data.network.utils.json.JsonArrayBuilder
import ua.ideabank.obank.data.network.utils.json.JsonObjectBuilder
import ua.ideabank.obank.data.network.utils.json.JsonArrayBody
import ua.ideabank.obank.data.network.utils.json.JsonObjectBody
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * A [converter][Converter.Factory] which uses Gson for JSON.
 *
 *
 * Because Gson is so flexible in the types it supports, this converter assumes that it can handle
 * all types. If you are mixing JSON serialization with something else (such as protocol buffers),
 * you must [map this instance][Retrofit.Builder.addConverterFactory]
 * last to allow the other converters a chance to see their types.
 */
internal class DslGsonConverterFactory private constructor(
	private val gson: Gson,
	private val gsonConverterFactory: GsonConverterFactory
) : Converter.Factory() {

	override fun responseBodyConverter(
		type: Type,
		annotations: Array<Annotation>,
		retrofit: Retrofit
	): Converter<ResponseBody, *>? {
		return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
	}

	override fun requestBodyConverter(
		type: Type,
		parameterAnnotations: Array<Annotation>,
		methodAnnotations: Array<Annotation>,
		retrofit: Retrofit
	): Converter<*, RequestBody> {
		return DslGsonRequestBodyConverter(
			gson,
			gson.getAdapter(TypeToken.get(type))
		)
	}

	private class DslGsonRequestBodyConverter<T>(
		private val gson: Gson,
		private val adapter: TypeAdapter<T>
	) : Converter<T, RequestBody> {

		@Throws(IOException::class)
		@Suppress("RemoveRedundantQualifierName", "UNCHECKED_CAST")
		override fun convert(value: T): RequestBody {
			val buffer = Buffer()
			val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
			val jsonWriter = gson.newJsonWriter(writer)

			kotlin.runCatching {
				value as? JsonObjectBody ?: throw Exception("Class cast exception")
				val builder = JsonObjectBuilder()
				value.invoke(builder)
				val adapter = gson.getAdapter(JsonObject::class.java)
				adapter.write(jsonWriter, builder.build())

			}.getOrNull() ?: kotlin.runCatching {
				value as? JsonArrayBody ?: throw Exception("Class cast exception")
				val builder = JsonArrayBuilder()
				value.invoke(builder)
				val adapter = gson.getAdapter(JsonArray::class.java)
				adapter.write(jsonWriter, builder.build())

			}.getOrNull() ?: adapter.write(jsonWriter, value)

			jsonWriter.close()
			return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
		}

		private companion object {
			private val MEDIA_TYPE = "application/json".toMediaType()
			private val UTF_8 = Charset.forName("UTF-8")
		}
	}

	companion object {
		/**
		 * Create an instance using a default [Gson] instance for conversion. Encoding to JSON and
		 * decoding from JSON (when no charset is specified by a header) will use UTF-8.
		 */
		@JvmStatic
		@JvmName("create")
		operator fun invoke() =
			invoke(Gson())

		/**
		 * Create an instance using `gson` for conversion. Encoding to JSON and
		 * decoding from JSON (when no charset is specified by a header) will use UTF-8.
		 */
		@JvmStatic
		@JvmName("create")
		operator fun invoke(gson: Gson) =
			DslGsonConverterFactory(
				gson,
				GsonConverterFactory.create(gson)
			)

	}
}
