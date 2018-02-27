package util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NsLookup {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		InetAddress[] ipsArr = null;
		while (true) {

			try {
				System.out.print(">>");
				String address = scanner.nextLine();

				if ("exit".equals(address)) {
					return;
				}

				ipsArr = InetAddress.getAllByName(address);

				for (InetAddress i : ipsArr) {
					System.out.println(i.getHostName() + " : " + i.getHostAddress());
				}

			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

		}

	}
}
