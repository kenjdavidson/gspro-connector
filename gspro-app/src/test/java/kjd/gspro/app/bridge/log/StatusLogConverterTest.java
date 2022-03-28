package kjd.gspro.app.bridge.log;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import kjd.gspro.api.Status;

@TestInstance(Lifecycle.PER_CLASS)
public class StatusLogConverterTest {

    StatusLogConverter converter;

    @BeforeAll
    void setup() {
        converter = new StatusLogConverter();
    }

    @Test 
    void testConvert_systemName_message() {
        LogEntry entry = converter.convert(this, Status.ok());
        
        assertEquals(entry.getSystemName().getValue(), "StatusLogConverterTest");
        assertEquals(entry.getMessage().getValue(), "Data received successfully");
    }

    @Test
    void testType_1xx() {
        LogEntry.Type type = converter.type(100);
        assertEquals(type, LogEntry.Type.INFO);
    }

    @Test
    void testType_2xx() {
        LogEntry.Type type = converter.type(200);
        assertEquals(type, LogEntry.Type.INFO);
    }

    @Test
    void testType_5xx() {
        LogEntry.Type type = converter.type(500);
        assertEquals(type, LogEntry.Type.ERROR);
    }
}
