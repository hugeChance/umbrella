package com.bohai.subAccount.swt.getWay;

public class GetWayHeartBeatThread implements Runnable{
	
	private GetWay getWay;
	
	public GetWayHeartBeatThread(GetWay getWay) {
		this.getWay = getWay;
	}

	@Override
	public void run() {

		while(true){
			
			getWay.heartBeat();
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
