package kjd.gspro.api.v1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import kjd.gspro.api.ConnectionException;
import kjd.gspro.api.Publisher;
import kjd.gspro.api.Status;

public class ConnectionImplTest {
    @Test
    public void isConnected_initialized() {
        Publisher<Status> publisher = spy(new Publisher<Status>());
        ConnectionImpl conn = new ConnectionImpl(publisher);

        assertFalse(conn.isConnected());
    }

    @Test 
    public void isConnected_connected() throws ConnectionException, IOException {
        Publisher<Status> publisher = spy(new Publisher<Status>());
        Socket socket = mock(Socket.class);                

        ConnectionImpl conn = new ConnectionImpl(publisher){
            @Override 
            protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
                return socket;
            }
        };

        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();

        conn.connect("192.168.1.1", 8080);

        assertTrue(conn.isConnected());
    }

    @Test 
    public void isCancelled_notConnected() {
        Publisher<Status> publisher = spy(new Publisher<Status>());
        ConnectionImpl conn = new ConnectionImpl(publisher);

        assertFalse(conn.isCancelled());
    }

    @Test 
    public void isCancelled_connected() throws ConnectionException, IOException {
        Publisher<Status> publisher = spy(new Publisher<Status>());
        Socket socket = mock(Socket.class);                

        ConnectionImpl conn = new ConnectionImpl(publisher){
            @Override 
            protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
                return socket;
            }
        };

        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();

        conn.connect("192.168.1.1", 8080);

        assertFalse(conn.isCancelled());
    }

    @Test 
    public void isCancelled_cancelled() throws ConnectionException, IOException {
        Publisher<Status> publisher = spy(new Publisher<Status>());
        Socket socket = mock(Socket.class);                

        ConnectionImpl conn = new ConnectionImpl(publisher){
            @Override 
            protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
                return socket;
            }
        };

        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();

        conn.connect("192.168.1.1", 8080);
        conn.cancel();

        assertTrue(conn.isCancelled());
    }

    @Test 
    public void connect() throws IOException, ConnectionException {
        Publisher<Status> publisher = spy(new Publisher<Status>());
        Socket socket = mock(Socket.class);                

        ConnectionImpl conn = new ConnectionImpl(publisher){
            @Override 
            protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
                return socket;
            }
        };

        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();

        conn.connect("192.168.1.1", 8080);

        assertTrue(conn.isConnected());
        verify(publisher, times(1)).publish(Status.connecting());
        verify(publisher, times(1)).publish(Status.connected());
    }

    @Test(expected = ConnectionException.class)
    public void connect_socketError_unknownHost() throws ConnectionException {
        Publisher<Status> publisher = spy(new Publisher<Status>());

        ConnectionImpl conn = new ConnectionImpl(publisher){
            @Override 
            protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
                throw new UnknownHostException();
            }
        };

        conn.connect("192.168.1.1", 8080);
    }

    @Test(expected = ConnectionException.class)
    public void connect_socketError_Io() throws ConnectionException {
        Publisher<Status> publisher = spy(new Publisher<Status>());

        ConnectionImpl conn = new ConnectionImpl(publisher){
            @Override 
            protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
                throw new IOException();
            }
        };

        conn.connect("192.168.1.1", 8080);
    }

    @Test(expected = ConnectionException.class)
    public void connect_Connected() throws IOException, ConnectionException {
        Publisher<Status> publisher = spy(new Publisher<Status>());
        Socket socket = mock(Socket.class);                

        ConnectionImpl conn = new ConnectionImpl(publisher){
            @Override 
            protected Socket createSocket(String host, Integer port) throws UnknownHostException, IOException {
                return socket;
            }
        };

        doReturn(mock(OutputStream.class)).when(socket).getOutputStream();
        doReturn(mock(InputStream.class)).when(socket).getInputStream();
        doReturn(true).when(socket).isConnected();

        conn.connect("192.168.1.1", 8080);
        conn.connect("192.168.1.1", 8080);
    }

    @Test
    public void testRun() {
        fail("Not yet implemented");
    }

    @Test
    public void testWrite() {
        fail("Not yet implemented");
    }
}
