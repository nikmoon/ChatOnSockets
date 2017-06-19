package nikpack;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Date;

/**
 * Created by nikbird on 19.06.2017.
 *
 *  UDP-сокет, ожидающий получения сообщений
 *  Запускается в отдельном потоке
 *
 */
public abstract class ListenSocket extends Thread {

    public abstract void onReceiveMessage(String message);

    private final static int timeout = 5000;

    private DatagramSocket socket;
    private DatagramPacket packet;
    private boolean stopListenFlag = false;

    public ListenSocket() throws SocketException {
        super("ListenSocket");
        socket = new DatagramSocket();
        packet = new DatagramPacket(new byte[1024], 1024);
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(timeout);
            for (;;)
                try {
                    socket.receive(packet);
                    onReceiveMessage(new String(packet.getData(), 0, packet.getLength()));
                } catch (SocketTimeoutException e) {
                    if (stopListenFlag)
                        break;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    /**
     *  Получить номер порта, к которому привязан слушающий сокет
     *
     * @return
     */
    public int getListenPort() {
        return socket.getLocalPort();
    }

    /**
     *  Просим поток слушателя корректно завершиться
     */
    public void stopListen() {
        stopListenFlag = true;
    }
}
