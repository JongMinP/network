package echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	private static final int SERVER_PORT = 6000;

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();

			// 2. 바인딩(Binding)
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));
			consoleLog("[server] binding " + localhostAddress + ":" + SERVER_PORT);

			while (true) {
				// 3. 연결 요청 기다림(accept)
				Socket socket = serverSocket.accept(); // blocking 대기상태
				new EchoServerReceiveThread(socket).start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null && serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void consoleLog(String log) {
		System.out.println("[server:" + Thread.currentThread().getId() + "] " + log);
	}

}
