package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	private static final int SERVER_PORT = 5000;

	public static void main(String[] args) {

		ServerSocket serverSocket = null;

		try {
			// 1. 서버소켓 생성
			serverSocket = new ServerSocket();

			// Time-wait 상태에서 서버 재실행이 가능하게 끔 함
			serverSocket.setReuseAddress(true);

			// 2. 바인딩(Binding)
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localhostAddress, SERVER_PORT));
			System.out.println("[server] binding " + localhostAddress + ":" + SERVER_PORT);

			// 3. 연결 요청 기다림(accept)
			Socket socket = serverSocket.accept(); // blocking 대기상태

			// 4. 연결 성공
			InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress(); // 나를 찌른 사람 주소
			int remoteHostPort = remoteSocketAddress.getPort();
			String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();

			System.out.println("[server] connected! from " + remoteHostAddress + ":" + remoteHostPort);

			try {
				// 5. I/O Stream 받아오기
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				while (true) {
					// 6. 데이터 읽기( read)
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); // Blocking
					// 멀티스레드로 accept랑

					if (readByteCount == -1) { // 정상종료
						System.out.println("[server] disconnected by client");
						break;
					}

					String data = new String(buffer, 0, readByteCount, "UTF-8");
					System.out.println("[server] received:" + data);

					// 7. 데이터 쓰기
					os.write(data.getBytes("UTF-8"));

				}

			} catch (SocketException e) {
				// 상대편이 정상적으로 소켓을 닫지 않고 종료한 경우
				System.out.println("[server] sudden closed by client");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
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

}
