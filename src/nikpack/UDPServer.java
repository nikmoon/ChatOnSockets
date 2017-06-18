package nikpack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
        DatagramPacket packet;
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            for(;;) {
                packet = new DatagramPacket(buffer, 1024);
                socket.receive(packet);
                System.out.println(new String(buffer, 0, packet.getLength()));
                System.out.println("from port: " + packet.getPort());

                packet = new DatagramPacket(packet.getData(), packet.getLength(),
                        InetAddress.getLoopbackAddress(), packet.getPort());
                socket.send(packet);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Socket exception");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
