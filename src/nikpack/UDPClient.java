package nikpack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by nikbird on 18.06.2017.
 */
public class UDPClient {

    public static void main(String[] args) {

        byte[] buffer = new byte[1024];
        ListenSocket listenSocket;

        // создаем слушающий сокет для приема сообщений
        try {
            listenSocket = new ListenSocket() {
                @Override
                public void onReceiveMessage(String message) {
                    System.out.println(message);
                }
            };
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        // запускаем слушающий сокет
        listenSocket.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, new InetSocketAddress("127.0.0.1", 5000));

        try (DatagramSocket socket = new DatagramSocket()) {

            // сообщаем серверу порт слушающего сокета
            String listenPortMessage = "LISTEN_PORT=" + String.valueOf(listenSocket.getListenPort());
            packet.setData(listenPortMessage.getBytes());
            socket.send(packet);

            for(;;) {

                // считываем сообщение пользователя
                String message = br.readLine();
                if (message.equals("/exit"))
                    break;

                // отправляем введенное пользователем значение на сервер
                packet.setData(message.getBytes());
                socket.send(packet);
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("Socket exception");
        } catch (IOException e) {
            e.printStackTrace();
        }

        listenSocket.stopListen();
        try {
            listenSocket.join(10000);
        } catch (InterruptedException e) {
            System.out.println("Listen thread not responding. Exit program.");
            System.exit(-1);
        }
    }
}
