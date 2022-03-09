package kjd.gspro.api.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;
import org.mockito.Mockito;

import kjd.gspro.api.ConnectionException;
import kjd.gspro.api.Publisher;
import kjd.gspro.api.Request;
import kjd.gspro.data.Units;

public class ClientImplTest {
    @Test
    public void testGetHost_defaultValue() {
        ClientImpl client = new ClientImpl();

        assertTrue(client.getHost().equals(ClientImpl.DEFAULT_HOST));
    }    

    @Test
    public void testGetHost_customValue() {
        ClientImpl client = new ClientImpl("192.168.1.1");

        assertTrue(client.getHost().equals("192.168.1.1"));
    }    

    @Test
    public void testGetPort_defaultPort() {
        ClientImpl client = new ClientImpl();

        assertTrue(client.getPort().equals(ClientImpl.DEFAULT_PORT));
    }

    @Test
    public void testGetPort_customPort() {
        ClientImpl client = new ClientImpl("192.168.1.1", 8080);

        assertTrue(client.getPort().equals(8080));
    }

    @Test
    public void testGetStatusPublisher() {
        ClientImpl client = new ClientImpl();

        assertNotNull(client.getStatusPublisher());
        assertTrue(client.getStatusPublisher() instanceof Publisher);
    }

    @Test
    public void testGetTimeout_defaultTimeout() {
        ClientImpl client = new ClientImpl();

        assertTrue(client.getTimeout().equals(ClientImpl.DEFAULT_TIMEOUT));
    }

    @Test
    public void testGetTimeout_customTimeout() {
        ClientImpl client = new ClientImpl("192.168.1.1", 8080, 600);

        assertTrue(client.getTimeout().equals(600));
    }

    @Test
    public void testIsConnected_initialized_noConnection() {
        ClientImpl client = new ClientImpl();

        assertFalse(client.isConnected());
    }

    @Test 
    public void testIsConnected_successfulConnection() throws ConnectionException, UnknownHostException, IOException {
        ConnectionImpl connection = mock(ConnectionImpl.class);
        ClientImpl client = client(connection);

        when(connection.isConnected()).thenReturn(true);
        when(connection.connect(Mockito.anyString(), Mockito.anyInt())).thenReturn(connection);

        client.connect();

        assertTrue(client.isConnected());
    }

    @Test
    public void testIsconnected_afterDisconnect() throws ConnectionException, UnknownHostException, IOException, InterruptedException {
        ConnectionImpl connection = mock(ConnectionImpl.class);
        ClientImpl client = client(connection);

        when(connection.isConnected()).thenReturn(true);
        when(connection.connect(Mockito.anyString(), Mockito.anyInt())).thenReturn(connection);

        client.connect();
        client.cancel();

        assertFalse(client.isConnected());
    }

    @Test(expected = ConnectionException.class)
    public void testSend_noConnection() throws ConnectionException {
        ConnectionImpl connection = mock(ConnectionImpl.class);
        ClientImpl client = client(connection);

        when(connection.isConnected()).thenReturn(true);
        when(connection.connect(Mockito.anyString(), Mockito.anyInt())).thenReturn(connection);

        client.send(request());
    }

    @Test
    public void testSend_connection_successful() throws ConnectionException {
        ConnectionImpl connection = mock(ConnectionImpl.class);
        ClientImpl client = client(connection);

        when(connection.isConnected()).thenReturn(true);
        when(connection.connect(Mockito.anyString(), Mockito.anyInt())).thenReturn(connection);

        client.connect();

        boolean sent = client.send(request());

        assertTrue(sent);
    }

    @Test(expected = ConnectionException.class)
    public void testSend_connection_failure() throws IOException, ConnectionException {
        ConnectionImpl connection = mock(ConnectionImpl.class);
        ClientImpl client = client(connection);

        when(connection.isConnected()).thenReturn(true);
        when(connection.connect(Mockito.anyString(), Mockito.anyInt())).thenReturn(connection);
        doThrow(ConnectionException.class).when(connection).write(Mockito.any());

        client.connect();
        client.send(request());
    }

    Request request() {
        return RequestBuilder.create("test", Units.YARDS)
            .heartbeat(true, true);
    }

    ClientImpl client(ConnectionImpl connection) {
        return new ClientImpl() {
            @Override
            protected ConnectionImpl createConnection() {
                return connection;
            }
        };
    }
}
