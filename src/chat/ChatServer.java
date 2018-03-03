package chat;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), SERVER_PORT));
			List<Writer> listWriter = new ArrayList<>();
			while (true) {
				Socket socket = serverSocket.accept();
				new ChatServerProcessThread(socket, listWriter).start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
