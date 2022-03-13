package kjd.gspro.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;
import org.mockito.Mockito;

import kjd.gspro.api.Request;
import kjd.gspro.api.RequestBuilder;
import kjd.gspro.data.Units;

public class ClientTest {
    @Test
    public void testGetHost_defaultValue() {
        Client client = new Client();

        assertTrue(client.getHost().equals(Client.DEFAULT_HOST));
    }    

    @Test
    public void getHost_customValue() {
        Client client = new Client("192.168.1.1");

        assertTrue(client.getHost().equals("192.168.1.1"));
    }    

    @Test
    public void getPort_defaultPort() {
        Client client = new Client();

        assertTrue(client.getPort().equals(Client.DEFAULT_PORT));
    }

    @Test
    public void getPort_customPort() {
        Client client = new Client("192.168.1.1", 8080);

        assertTrue(client.getPort().equals(8080));
    }

    @Test
    public void getTimeout_defaultTimeout() {
        Client client = new Client();

        assertTrue(client.getTimeout().equals(Client.DEFAULT_TIMEOUT));
    }

    @Test
    public void getTimeout_customTimeout() {
        Client client = new Client("192.168.1.1", 8080, 600);

        assertTrue(client.getTimeout().equals(600));
    }

    @Test
    public void testIsConnected_initialized_noConnection() {
        Client client = new Client();

        assertFalse(client.isConnected());
    }

    @Test 
    public void isConnected_successfulConnection() throws ConnectionException, UnknownHostException, IOException {
        Connection connection = mock(Connection.class);
        Client client = client(connection);

        when(connection.isConnected()).thenReturn(true);

        client.connect();

        assertTrue(client.isConnected());
    }

    @Test
    public void isconnected_afterDisconnect() throws ConnectionException, UnknownHostException, IOException, InterruptedException {
        Connection connection = mock(Connection.class);
        Client client = client(connection);

        when(connection.isConnected()).thenReturn(true);

        client.connect();
        client.disconnect();

        assertFalse(client.isConnected());
    }

    @Test(expected = ConnectionException.class)
    public void send_noConnection() throws ConnectionException {
        Connection connection = mock(Connection.class);
        Client client = client(connection);

        when(connection.isConnected()).thenReturn(true);

        client.send(request());
    }

    @Test
    public void send_connection_successful() throws ConnectionException {
        Connection connection = mock(Connection.class);
        Client client = client(connection);

        when(connection.isConnected()).thenReturn(true);

        client.connect();

        boolean sent = client.send(request());

        assertTrue(sent);
    }

    @Test(expected = ConnectionException.class)
    public void send_connection_failure() throws IOException, ConnectionException {
        Connection connection = mock(Connection.class);
        Client client = client(connection);

        when(connection.isConnected()).thenReturn(true);
        doThrow(ConnectionException.class).when(connection).write(Mockito.any());

        client.connect();
        client.send(request());
    }

    Request request() {
        return RequestBuilder.create("test", Units.YARDS)
            .heartbeat(true, true);
    }

    Client client(Connection connection) {
        return new Client() {
            @Override
            protected Connection createConnection() {
                return connection;
            }
        };
    }
}
