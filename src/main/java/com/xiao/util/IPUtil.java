package com.xiao.util;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.*;

public class IPUtil {
    //无效的ip 返回true 有效的ip返回false
   public static boolean ifUseless(String ip, int port) {
        URL url = null;
        try {
            url = new URL("http://www.baidu.com");
            InetSocketAddress addr = new InetSocketAddress(ip, port);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            InputStream in = null;
            try {
                URLConnection conn = url.openConnection(proxy);
                conn.setConnectTimeout(2000);
                in = conn.getInputStream();
            } catch (Exception e) {
                return true;
            }
            String s = IOUtils.toString(in);
            if (s.indexOf("baidu") > 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
