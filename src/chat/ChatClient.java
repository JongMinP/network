package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	private static final String SERVER_IP = "192.168.56.1";
	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {

		Socket socket = new Socket();
		Scanner scanner = new Scanner(System.in);

		// 2개이상 클라이언트랑 통신 하려면 멀티스레드

		try {
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));

			// 문자를 개행단위로 읽기 위해
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

			String message = null;
			String echo = null;

			while ((message = br.readLine()) != null) {
				System.out.println(">>");

				if ("exit".equals(message)) {
					break;
				}

				pw.println(message); // 서버에 내 메세지 전달
				pw.flush();

				echo = br.readLine();

				System.out.println("서버<<" + echo);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			scanner.close();
		}
	}
}
