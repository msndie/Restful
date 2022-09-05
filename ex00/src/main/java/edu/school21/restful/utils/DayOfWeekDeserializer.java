package edu.school21.restful.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.DayOfWeek;

public class DayOfWeekDeserializer extends JsonDeserializer<DayOfWeek> {

    @Override
    public DayOfWeek deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
//        String day = node.get("dayOfWeek").asText();
//        return DayOfWeek.valueOf(day.toUpperCase());
        return DayOfWeek.valueOf(node.asText().toUpperCase());
    }
}
