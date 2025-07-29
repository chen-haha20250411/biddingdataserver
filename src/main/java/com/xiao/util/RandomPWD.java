package com.xiao.util;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class RandomPWD {



	/**
	 * 生成随机数字
	 * */
	public static String getCode(){
		//毫秒级100年内12位数不重复
		long id =System.currentTimeMillis()-1300000000000L;
		return String.valueOf(id);
	}

	 /**
	  * 生成15位随机数字
	  * @param len生成数字的长度
	  * @return 数字字符串返回
	  */
	 public static String genrandomNum(int len){
		 char[] arrChar = new char[]{ '0','1','2','3','4','5','6','7','8','9' };
			StringBuilder num = new StringBuilder();
			Random rnd = new Random();
			for (int i = 0; i < len; i++) {
			 num.append(arrChar[rnd.nextInt(10)]);
			}
		 return num.toString();
	 }
	 /**
	  * 生成随即密码(包含字母)
	  * @param pwd_len 生成的密码的总长度
	  * @return  密码的字符串
	  */
	 public static String genRandomstring(int pwd_len){
	  //35是因为数组是从0开始的，26个字母+10个数字
	  final int  maxNum = 36;
	  int i;  //生成的随机数
	  int count = 0; //生成的密码的长度
	  char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
	    'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
	    'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	  StringBuffer pwd = new StringBuffer("");
	  Random r = new Random();
	  while(count < pwd_len){
	   //生成随机数，取绝对值，防止生成负数，

	   i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1

	   if (i >= 0 && i < str.length) {
	    pwd.append(str[i]);
	    count ++;
	   }
	  }

	  return pwd.toString();
	 }
	 /**
	  * 生成系统单号
	  * @return
	  */
	public static String buildOrderNo(){
		Calendar c1 = Calendar.getInstance();
		//取系统当前时间
        c1.setTime(new Date());
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");//设置日期格式
		String datetime = df.format(c1.getTime());//17位
		System.out.println("datetime = "+datetime);
		String order_no = "";
		DecimalFormat df1=new DecimalFormat("000");
		Random random = new Random();
		//产生一个0-999的随机整数
		long randomNum = random.nextInt(1000);
		String num = df1.format(randomNum);
		//生成唯一的订单编号(order_no)
		order_no = datetime+num;
		return order_no;
	}
}

