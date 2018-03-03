package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerProcessThread extends Thread {
	private String nickname;
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private List<Writer> listWriter;

	public ChatServerProcessThread(Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriter = listWriters;
	}

	@Override
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

			while (true) {
				String request = br.readLine();
				if (request == null) {
					System.out.println("연결 끝");
					doQuit(pw);
					break;
				}

				String[] tokens = request.split(":");

				if ("join".equals(tokens[0])) {
					doJoin(tokens[1], pw);
				} else if ("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				}
				if ("quit".equals(tokens[0])) {
					doQuit(pw);
				} else {
					System.out.println("에러에러" + tokens[0]);

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			AutoClose.closeResource(br, pw);
		}

	}

	private void doJoin(String nickName, Writer writer) {
		this.nickname = nickName;

		String data = nickName + "님이 참여하였습니다.";
		broadcast(data);

		addWriter(writer);

	}

	private void doMessage(String message) {
		String data = nickname + ":" + message;
		broadcast(data);

	}

	private void addWriter(Writer writer) {
		synchronized (listWriter) {
			listWriter.add(writer);
		}
	}

	private void broadcast(String data) {
		synchronized (listWriter) {
			for (Writer writer : listWriter) {
				PrintWriter printWriter = (PrintWriter) writer;
				printWriter.println(data);
				printWriter.flush();
			}
		}
	}

	private void doQuit(Writer writer) {
		removeWriter(writer);

		String data = nickname + "님이 퇴장 하였습니다.";
		broadcast(data);
	}

	private void removeWriter(Writer writer) {
		synchronized (listWriter) {
			listWriter.remove(writer);
		}
	}

}
