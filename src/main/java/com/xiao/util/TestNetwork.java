package com.xiao.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
* 说明：测试服务器Ip 端口是否通畅
* @author  赵增丰
* @version  1.0
* 2016年1月6日 下午2:48:12
*/ 
public class TestNetwork extends Thread{
	
	private String msg = "";
	
	public static void main(String[] args) {
		
		TestNetwork TestNetwork = new TestNetwork();
		TestNetwork.run();
	}


	boolean isReachable(String remoteInetAddr,int port, int timeout) {
		boolean isReachable = false;
		Socket socket = null;
		try {
			socket = new Socket(); 
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(remoteInetAddr, port);
			socket.connect(endpointSocketAddr, timeout);
			System.out.println("SUCCESS -connection established!  remote: "+ remoteInetAddr + " port " + port);
			isReachable = true;
		} catch (IOException e) {
			msg = msg +"FAILRE - CAN not connect!  remote: "+ remoteInetAddr + "port " + port ;
			System.out.println("FAILRE - CAN not connect!  remote: "+ remoteInetAddr + "port " + port);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Error occurred while closing socket..");
				}
			}
		}
		return isReachable;
	}


	@Override
	public void run() {
		while(true){
			try{
				msg = "";
//				this.isReachable("120.55.241.183", 3306, 6000);//超时6秒
//				this.isReachable("120.55.240.138", 8080, 6000);
				this.isReachable("120.55.241.2", 8090, 6000);
				this.isReachable("120.55.241.18", 8080, 6000);
				
				this.isReachable("120.26.129.249", 8080, 6000);//流水线
				this.isReachable("120.26.129.249", 8090, 6000);//处理库存
				
				this.isReachable("120.55.241.18", 27017, 6000);
				//GTS server
//				this.isReachable("120.55.241.189", 80, 6000);
//				this.isReachable("120.55.241.176", 3306, 6000);
				/*if(!"".equals(msg)){
					MailUtil.sendEmail("heb@zzheng.com.cn",  msg);
					MailUtil.sendEmail("zhaozengfeng@glenet.com",  msg);
				}
				Thread.sleep(300000);//˯5分钟 发一次邮件
*/			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

}
