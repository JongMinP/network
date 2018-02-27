package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class ChatServerThread extends Thread {

	private Socket socket;

	public ChatServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// 4. 연결 성공
		InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress(); // 나를 찌른 사람 주소
		int remoteHostPort = remoteSocketAddress.getPort();
		String remoteHostAddress = remoteSocketAddress.getAddress().getHostAddress();

		consoleLog("[server] connected! from " + remoteHostAddress + ":" + remoteHostPort);

		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			// 5. I/O Stream 받아오기

			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

			String line = null;

			while ((line = br.readLine()) != null) {

				// // 멀티스레드로 accept랑
				consoleLog("[server] received:" + line);
				// 7. 데이터 쓰기
				pw.println(line); // 클라 전송
				pw.flush(); // 버퍼 비우기

			}

		} catch (SocketException e) {
			// 상대편이 정상적으로 소켓을 닫지 않고 종료한 경우
			consoleLog("[server] sudden closed by client");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (pw != null)
					pw.close();
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void consoleLog(String log) {
		System.out.println("[server:" + getId() + "] " + log);
	}
}
