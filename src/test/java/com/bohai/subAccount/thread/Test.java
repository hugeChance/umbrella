package com.bohai.subAccount.thread;

public class Test {
	
	static Integer i = 0;
	
	public static void main(String[] args) {
		
			for (int i = 0; i < 10; i++) {
				
				Thread thread = new Thread(new Increament());
				thread.start();
			}
	}
	
	public static class Increament implements Runnable{

		@Override
		public void run() {
			
			System.out.println(i++);
			
		}
		
		
	}

}
