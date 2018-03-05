package time;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPTimeClient {

	// private static final String SERVER_IP = "192.168.1.21";
	private static final int SERVER_PORT = 6000;
	private static final int BUFFER_SIZE = 1024;
	private static final String ECODING = "UTF-8";

	public static void main(String[] args) {
		DatagramSocket socket = null;
		Scanner scanner = null;

		try {
			scanner = new Scanner(System.in);

			socket = new DatagramSocket();

			while (true) {

				System.out.print(">>");
				String message = scanner.nextLine();

				if ("quit".equals(message)) {
					break;
				}

				if ("".equals(message)) {

					byte[] sendData = message.getBytes(ECODING);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
							new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), SERVER_PORT));

					socket.send(sendPacket);

					DatagramPacket receviePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);

					message = new String(receviePacket.getData(), 0, receviePacket.getLength(), ECODING);
					System.out.println("<<" + message);
				}

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
