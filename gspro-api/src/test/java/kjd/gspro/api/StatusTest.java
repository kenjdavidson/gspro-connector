package kjd.gspro.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.Test;

import kjd.gspro.data.Club;
import kjd.gspro.data.Hand;

public class StatusTest {

    @Test
    public void serialize_status_connected() throws JsonProcessingException {
        Status status = Status.connected();

        assertEquals("{\"Code\":100,\"Message\":\"Connected to GS Pro!\",\"Connected\":true}", Json.writeValue(status));
    }

    @Test
    public void serialize_status_connecting() throws JsonProcessingException {
        Status status = Status.connecting();

        assertEquals("{\"Code\":100,\"Message\":\"Connecting to GS Pro...\",\"Connected\":false}", Json.writeValue(status));
    }

    @Test
    public void serialize_status_disconnected() throws JsonProcessingException {
        Status status = Status.disconnected();

        assertEquals("{\"Code\":100,\"Message\":\"Disconnected.\",\"Connected\":false}", Json.writeValue(status));
    }

    @Test
    public void deserialize_status_connected() throws JsonProcessingException {
        Status status = Json.readValue("{\"Code\":100,\"Message\":\"Connected to GS Pro!\",\"Connected\":true}", Status.class);

        assertTrue(100 == status.getCode());
        assertTrue(status.isConnected());
        assertEquals("Connected to GS Pro!", status.getMessage());        
    }

    @Test
    public void deserialize_status_connecting() throws JsonProcessingException {
        Status status = Json.readValue("{\"Code\":100,\"Message\":\"Connecting to GS Pro...\",\"Connected\":false}", Status.class);

        assertTrue(100 == status.getCode());
        assertFalse(status.isConnected());
        assertEquals("Connecting to GS Pro...", status.getMessage());        
    }

    @Test
    public void deserialize_status_disconnected() throws JsonProcessingException {
        Status status = Json.readValue("{\"Code\":100,\"Message\":\"Disconnected.\",\"Connected\":false}", Status.class);

        assertTrue(100 == status.getCode());
        assertFalse(status.isConnected());
        assertEquals("Disconnected.", status.getMessage());        
    }

    @Test
    public void deserialize_status_success() throws JsonMappingException, JsonProcessingException {
        Status status = Json.readValue("{\"Code\":200,\"Message\":\"Shot received successfully\",\"Connected\":true}", Status.class);

        assertTrue(200 == status.getCode());
        assertTrue(status.isConnected());
        assertEquals("Shot received successfully", status.getMessage());        
    }

        @Test
    public void deserialize_status_playerInfo() throws JsonMappingException, JsonProcessingException {
        Status status = Json.readValue("{\"Code\":201,\"Message\":\"GSPro Player Information\",\"Player\":{\"Handed\": \"RH\",\"Club\":\"DR\"},\"Connected\":true}", Status.class);

        assertTrue(201 == status.getCode());
        assertTrue(status.isConnected());
        assertEquals("GSPro Player Information", status.getMessage()); 
        assertNotNull(status.getPlayer());       
        assertTrue(status.getPlayer().getHanded() == Hand.RIGHT);
        assertTrue(status.getPlayer().getClub() == Club.DRIVER);
    }
}
