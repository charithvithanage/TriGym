package com.example.charith.trigym.Convertors;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
    public Boolean deserialize(JsonElement json, Type typeOfT,
                               JsonDeserializationContext context) throws JsonParseException {
        Integer code = null;

        if (json.getAsString().equals("true")) {
            code = 1;
        } else if (json.getAsString().equals("false")) {
            code = 0;
        } else {
            code = json.getAsInt();

        }
        return code == 0 ? false :
                code == 1 ? true :
                        null;
    }
}