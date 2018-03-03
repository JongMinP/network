package chat;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClientApp {

	private static final int SERVER_PORT = 7000;

	public static void main(String[] args) {
		String name = null;
		Scanner scanner = new Scanner(System.in);
		Socket socket = null;

		try {

			socket = new Socket();
			socket.connect(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), SERVER_PORT));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {

				System.out.println("대화명을 입력하세요.");
				System.out.print(">>> ");
				name = scanner.nextLine();

				String data = "join:" + name;
				pw.println(data);
				pw.flush();

				if (name.isEmpty() == false) {
					break;
				}

				System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
			}
			new ChatWindow(name, socket).show();
		} catch (SocketException e) {
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			AutoClose.closeResource(scanner);

		}

	}

}
