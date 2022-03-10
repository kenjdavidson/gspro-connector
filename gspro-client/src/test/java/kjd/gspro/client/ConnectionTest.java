package kjd.gspro.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import kjd.gspro.api.Publisher;
import kjd.gspro.api.Status;

public class ConnectionTest {
    static final String HOST = Client.DEFAULT_HOST;
    static final Integer PORT = Client.DEFAULT_PORT;

    Publisher<Status> publisher;
    Connection connection;    

    @Before
    public void setup() {
        publisher = spy(new Publisher<Status>());
        connection = spy(new Connection(HOST, PORT, publisher));
    }

    @Test
    public void isConnected_initialized() {
        assertFalse(connection.isConnected());
    }

    @Test 
    public void isConnected_connected() throws ConnectionException, IOException {
        Socket socket = mock(Socket.class);                

        doReturn(socket).when(connection).createSocket(HOST, PORT);
        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();

        connection.connect();

        assertTrue(connection.isConnected());
    }

    @Test
    public void isConnected_disconnected() throws UnknownHostException, IOException {
        Socket socket = mock(Socket.class);                

        doReturn(socket).when(connection).createSocket(HOST, PORT);
        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();

        connection.connect();
        connection.disconnect();

        assertFalse(connection.isConnected());        
    }

    @Test 
    public void connect_publishesStatus() throws IOException, ConnectionException {
        Socket socket = mock(Socket.class);                

        doReturn(socket).when(connection).createSocket(HOST, PORT);
        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();

        connection.connect();

        verify(publisher, times(1)).publish(Status.connecting());
        verify(publisher, times(1)).publish(Status.connected());
    }

    @Test(expected = UnknownHostException.class)
    public void connect_socketError_unknownHost() throws UnknownHostException, IOException {    
        doThrow(UnknownHostException.class).when(connection).createSocket(HOST, PORT);

        connection.connect();
    }

    @Test(expected = IOException.class)
    public void connect_socketError_Io() throws UnknownHostException, IOException {
        doThrow(IOException.class).when(connection).createSocket(HOST, PORT);

        connection.connect();
    }

    @Test @Ignore
    public void parse_completeMessage() {
        fail("Not yet implemented");
    }

    @Test @Ignore
    public void parse_invalidMessage() {
        fail("Not yet implemented");
    }

    @Test @Ignore
    public void parse_completeMessage_withRemaining() {
        fail("Not yet implemented");
    }
}
