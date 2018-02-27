package echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import javax.print.attribute.standard.Media;

public class EchoServerReceiveThread extends Thread {

	private Socket socket;

	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// 4. 연결 성공
		InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress(); // 나를 찌른 사람 주소
		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();

		consoleLog("[server] connected! from " + remoteHostAddress + ":" + remoteHostPort);

		try {
			// 5. I/O Stream 받아오기
			// InputStream is = socket.getInputStream();
			// OutputStream os = socket.getOutputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),true);

			while (true) {

				// // 6. 데이터 읽기( read)
				// byte[] buffer = new byte[256];
				// int readByteCount = is.read(buffer); // Blocking
				String line = br.readLine();
				//
				if (line == null)
					break;
				// // 멀티스레드로 accept랑
				//
				// if (readByteCount == -1) { // 정상종료
				// consoleLog("[server] disconnected by client");
				// break;
				// }
				//
				// String data = new String(buffer, 0, readByteCount, "UTF-8");
				 consoleLog("[server] received:" + line);
				//
				// // 7. 데이터 쓰기
				// os.write(data.getBytes("UTF-8"));
				pw.println(line);
			}

		} catch (SocketException e) {
			// 상대편이 정상적으로 소켓을 닫지 않고 종료한 경우
			consoleLog("[server] sudden closed by client");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null && socket.isClosed() == false) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void consoleLog(String log) {
		System.out.println("[server:" + getId() + "] " + log);
	}
}
