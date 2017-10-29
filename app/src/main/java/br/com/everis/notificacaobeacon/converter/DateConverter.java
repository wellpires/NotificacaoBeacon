package br.com.everis.notificacaobeacon.converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.everis.notificacaobeacon.utils.Constants;

public class DateConverter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	private final DateFormat dateFormat;

	public DateConverter() {
		dateFormat = new SimpleDateFormat(Constants.DATETIME_PATTERN, Locale.US);
	}

	@Override
	public Date deserialize(JsonElement jsonElement, Type paramType, JsonDeserializationContext paramJsonDeserializationContext) throws JsonParseException {
		try {
			return dateFormat.parse(jsonElement.getAsString());
		} catch (ParseException e) {
			throw new JsonParseException(e);
		}
	}

	@Override
	public JsonElement serialize(Date date, Type paramType, JsonSerializationContext paramJsonSerializationContext) {
		return new JsonPrimitive(dateFormat.format(date));
	}

}
