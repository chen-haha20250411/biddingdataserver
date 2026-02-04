#biddingdataserver
1、项目部署 Maven 先clean 再 install
2、复制 target 目录下的三个文件 到 unbantu 系统路径中  /data/web/biddingserver/
3、发布服务 
   nohup java -server -jar /data/web/biddingserver/biddingdataserver.jar > /data/web/biddingserver/output.log 2>&1 & 

   查看输出
   tail -f /data/web/biddingserver/output.log
   查看java 服务
    ps aux | grep biddingdataserver.jar
   关闭服务
    kill -9 <PID>
 4、启动redis  默认自动启动  unbantu系统需要下载redis 
    systemctl status redis-server
