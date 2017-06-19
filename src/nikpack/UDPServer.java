package nikpack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

import static javafx.scene.input.KeyCode.H;

/**
 * Created by nikbird on 18.06.2017.
 */
public class UDPServer extends Thread {

    private int port;

    public static void main(String[] args) {
        new UDPServer(5000).start();
    }

    public UDPServer(int port) {
        super("UDPServer");
        this.port = port;
    }

    @Override
    public void run() {
        Map<Integer, Integer> clientListenPorts = new HashMap<>();
        DatagramPacket packet;
        byte[] buffer = new byte[1024];
        String message;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            for(;;) {

                Integer senderPort;
                Integer listenPort;

                // ожидаем сообщения
                packet = new DatagramPacket(buffer, 1024);
                socket.receive(packet);
                message = new String(buffer, 0, packet.getLength());
                System.out.println(message);
                senderPort = packet.getPort();
                if (message.startsWith("LISTEN_PORT=")) {
                    listenPort = Integer.valueOf(message.replace("LISTEN_PORT=", ""));
                    clientListenPorts.put(senderPort, listenPort);
                    continue;
                }

                // рассылаем сообщение всем клиентам
                for(Map.Entry<Integer, Integer> entry: clientListenPorts.entrySet()) {

                    // клиенту, приславшему сообщение, не посылаем
                    if (!entry.getKey().equals(senderPort)) {
                        packet.setPort(entry.getValue());
                        socket.send(packet);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Socket exception");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
