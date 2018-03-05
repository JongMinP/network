package echo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPEchoClient {

	private static final String SERVER_IP = "192.168.1.21";
	private static final int SERVER_PORT = 5000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {

		DatagramSocket socket = null;
		Scanner scanner = null;

		try {
			// 0. 키보드 연결
			scanner = new Scanner(System.in);

			// 1. 소켓 생성
			socket = new DatagramSocket();
			
//			System.out.println(InetAddress.getLocalHost().getHostAddress());

			while (true) {
				// 2. 사용자 입력
				System.out.print(">>");
				String message = scanner.nextLine();

				if ("".equals(message)) {
					continue;
				}

				if ("quit".equals(message)) {
					break;
				}
				// 3. 전송 패킷 생성
				byte[] sendData = message.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), SERVER_PORT));

				// 4. 전송
				socket.send(sendPacket);

				// 5. 메세지 수신
				DatagramPacket receviePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receviePacket);

				message = new String(receviePacket.getData(), 0, receviePacket.getLength(), "UTF-8");

				System.out.println("<<" + message);

			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}

	}
}
