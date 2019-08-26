package com.example.charith.trigym.Convertors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;

/**
 * Created by charith on 6/14/2017.
 */

public class DateTimeSerializer implements JsonDeserializer<DateTime>, JsonSerializer<DateTime>{
    //    private static final DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.dateTime();
    DateTimeZone utc = DateTimeZone.UTC;
    private static DateTimeFormatter DATE_FORMAT = ISODateTimeFormat.dateTimeNoMillis();


    @Override
    public DateTime deserialize(final JsonElement je, final Type type,
                                final JsonDeserializationContext jdc) throws JsonParseException {
        final String dateAsString = je.getAsString();


        if (dateAsString.length() == 0) {
            return null;
        } else {

            if (je.getAsString().contains(".")) {
                DATE_FORMAT = ISODateTimeFormat.dateTime();
            } else {
                DATE_FORMAT = ISODateTimeFormat.dateTimeNoMillis();
            }

            return DATE_FORMAT.parseDateTime(dateAsString);
        }
    }

    @Override
    public JsonElement serialize(final DateTime src, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        String retVal;
        if (src == null) {
            retVal = "";
        } else {
            retVal = DATE_FORMAT.print(src.withZone(utc));
        }
        return new JsonPrimitive(retVal);
    }

    private String removeMilliseconds(String str) {

        if (str.contains(".")) {
            String[] strings = str.split("\\.");
            String str1 = strings[0];
            String str2 = strings[1];
            str = str1 + str2.substring(3, 4);
        }
        return str;
    }
}