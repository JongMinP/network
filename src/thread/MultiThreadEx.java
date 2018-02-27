package thread;

public class MultiThreadEx {
	public static void main(String[] args) {

		Thread thread1 = new AlphabetThread();
		Thread thread2 = new Thread(new DigitThread());
		
		thread1.start();
		thread2.start();
		
		
//		for (int i = 0; i < 10; i++) {
//			System.out.print(i);
//		}

//		for (char c = 'a'; c < 'z'; c++) {
//			System.out.print(c);
//		}

	}
}
