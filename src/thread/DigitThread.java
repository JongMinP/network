package thread;

public class DigitThread implements Runnable {

	@Override
	public void run() {

		for (int i = 0; i < 10; i++) {
			System.out.print(i);

		}
	}

}
