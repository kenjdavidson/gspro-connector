package kjd.gspro.api;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

public class StatusTest {

    ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void serialize_StatusTest() throws JsonProcessingException {
        Status status = Status.builder()
            .connected(true)
            .message("Connected")
            .build();

        assertEquals("{\"IsConnected\":true,\"Message\":\"Connected\"}", mapper.writeValueAsString(status));
    }
}
