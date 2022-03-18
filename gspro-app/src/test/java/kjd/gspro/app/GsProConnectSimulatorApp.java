package kjd.gspro.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import kjd.gspro.api.Connection;
import kjd.gspro.api.Json;
import kjd.gspro.api.Request;
import kjd.gspro.api.Status;
import kjd.gspro.data.Club;
import kjd.gspro.data.Hand;
import kjd.gspro.data.Player;

/**
 * Simulates startup and processing of messages (as I assume the GS Pro Connect API) would work. This
 * includes:
 * <ul>
 *  <li>Starting a ServerSocket and accepting a single request (only one client allowed)</li>
 *  <li>Creates 2 players, one right hand, one left hand</li>
 *  <li>Accepts requests:
 *      <ul>
 *          <li>Every fifth shot fails with some error</li>
 *          <li>All others are just accepted and status okay'd back</li>
 *          <li>After each accepted shot, the player change is sent</li>
 *      <ul>
 *  </li>
 * </ul>
 * <p>
 * I'm not entirely sure how GS Pro notifies of shutting down.  The documentation shows that the client
 * will send continual heartbeats.  This includes a kind of reverse heartbeat that sends a basic
 * Status saying that the device is still connected.  It uses the status 101 (connected) since it 
 * doesn't make much sense to reply with a 200 (successful message).
 * 
 * @author kenjdavidson
 */
public class GsProConnectSimulatorApp {

    static void log(String message, Object...args) {
        System.out.println(MessageFormat.format(message, args));
    }

    public static void main(String[] args) {
        log("Starting simulator app, waiting on connection from client...");

        try (ServerSocket ss = new ServerSocket(Connection.DEFAULT_PORT);
                Socket socket = ss.accept()) {                    
            log("GS Pro Connector Client connected, starting simulator");

            GsProConnectSimulator simulator = new GsProConnectSimulator(socket);
            Thread thread = new Thread(simulator);
            thread.start();
            thread.join();
        } catch(IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log("GS Pro simulator shutdown");
        }
    }

    static class GsProConnectSimulator implements Runnable {

        final Socket socket;
        final LinkedList<Player> players;
        int count = 0;    

        public GsProConnectSimulator(Socket socket) {
            this.socket = socket;
            this.players = new LinkedList<>();
            this.players.offer(new Player(Hand.RIGHT, Club.DRIVER, 0f));
            this.players.offer(new Player(Hand.LEFT, Club.DRIVER, 0f));
        }

        @Override
        public void run() {
            try (final InputStream is = socket.getInputStream()) {

                sendPlayer();

                while (!(socket.isClosed())) {
                    Optional<Request> request = readRequest(is);
                    Status status = request.map(this::handleRequest).orElse(Status.ok());
                    sendStatus(status);

                    if (status.getCode() < 500) {
                        sendPlayer();
                    }

                    try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// Ignore and continue
					}
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Optional<Request> readRequest(InputStream is) throws IOException {
            StringBuilder data = new StringBuilder();
            int read, stack = 0;

            while ((read = is.read()) > -1) {
                if (((char)read) == '{') stack++;
                if (((char)read) == '}') stack--;

                data.append(String.valueOf((char)read));
                if (stack == 0) {
                    log("Attempting to parse {0}", data.toString());
                    
                    String dataString = data.toString();                    
                    data.delete(0, data.length());

                    return Optional.ofNullable(Json.readValue(dataString, Request.class)); 
                }
            }

            return Optional.empty();
        }

        Status handleRequest(Request request) {
            count++;

            if (count % 5 == 0) {
                return Status.error(new Exception("Issue with request"), true);
            } 

            return Status.ok();
        }

        void sendPlayer() throws JsonProcessingException, IOException {
            Player player = players.poll();
            players.offer(player);

            Status status = Status.player(player);
            socket.getOutputStream().write(Json.writeValue(status).getBytes());    
        }

        void sendStatus(Status status) throws JsonProcessingException, IOException {
            socket.getOutputStream().write(Json.writeValue(status).getBytes());
        }

        public void disconnect() {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
