package nikpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.Buffer;

/**
 * Created by nikbird on 18.06.2017.
 */
public class UDPClient {

    public static void main(String[] args) {
        InetAddress serverAddress;
        DatagramPacket packet;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        byte[] data;
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket()) {
            serverAddress = Inet4Address.getByName("127.0.0.1");
            for(;;) {

                data = br.readLine().getBytes();

                // отправляем строку на сервер
                packet = new DatagramPacket(data, data.length, serverAddress, 5000);
                socket.send(packet);

                // получаем ответ от сервера
                packet = new DatagramPacket(buffer, 1024);
                socket.receive(packet);
                System.out.println(new String(packet.getData(), 0, packet.getLength()));
                System.out.println("from port: " + packet.getPort());
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Socket exception");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
