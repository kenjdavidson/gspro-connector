package kjd.gspro.api.v1;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * Standardize JSON functionality.
 * 
 * @author kenjdavidson
 */
public final class Json {
    public static ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        MAPPER.configure(Feature.IGNORE_UNKNOWN, true);
        MAPPER.setSerializationInclusion(Include.NON_NULL);
    }

    public static <T> T readValue(String value, Class<T> clazz) throws JsonMappingException, JsonProcessingException {
        return MAPPER.readValue(value, clazz);
    }

    public static <T> String writeValue(T object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
}
