package com.xiao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil {
	FTPClient ftpClient;

	private final static Log logger = LogFactory.getLog(FtpUtil.class);


	/**
	 * 获取FTPClient对象
	 *
	 * @param ftpHost
	 *            FTP主机服务器
	 * @param ftpPassword
	 *            FTP 登录密码
	 * @param ftpUserName
	 *            FTP登录用户名
	 * @param ftpPort
	 *            FTP端口 默认为21
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, String ftpUserName, String ftpPassword, int ftpPort) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
			/** 设置传输的文件类型
             * BINARY_FILE_TYPE：二进制文件类型
             * ASCII_FILE_TYPE：ASCII传输方式，这是默认的方式
             * ....
             */
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.setControlEncoding("utf-8");
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				logger.info("未连接到FTP，用户名或密码错误。");
				ftpClient.abort();
				ftpClient.disconnect();
			} else {
				logger.info("FTP连接成功。");
			}
		} catch (SocketException e) {
			e.printStackTrace();
			logger.info("FTP的IP地址可能错误，请正确配置。");
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("FTP的端口错误,请正确配置。");
		}
		return ftpClient;
	}

	/**
	 * 下载文件
	 *
	 * @param ftpHost
	 *            ftp服务器地址
	 * @param ftpUserName
	 *            anonymous匿名用户登录，不需要密码。administrator指定用户登录
	 * @param ftpPassword
	 *            指定用户密码
	 * @param ftpPort
	 *            ftp服务员器端口号
	 * @param ftpPath
	 *            ftp文件存放物理路径
	 * @param fileName
	 *            文件路径
	 * @param input
	 *            文件输入流，即从本地服务器读取文件的IO输入流
	 */
	public static void downloadFile(FTPClient ftpClient, String localPath,String ftpPath) {
		// FTPClient ftpClient = null;
		if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            logger.debug("ftp 连接已经关闭或者连接无效......");
            return ;
        }
		try {
			// ftpClient = getFTPClient(ftpHost, ftpUserName, ftpPassword,
			// ftpPort);
			//ftpClient.setControlEncoding("UTF-8"); // 中文支持
			//ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			/**没有对应路径时，FTPFile[] 大小为0，不会为null*/
            FTPFile[] ftpFiles = ftpClient.listFiles(ftpPath);
            FTPFile ftpFile = null;
            if (ftpFiles.length >= 1) {
                ftpFile = ftpFiles[0];
            }
            if (ftpFile != null && ftpFile.isFile()) {
            	File localFile = new File(localPath, ftpPath);
                if (!localFile.getParentFile().exists()) {
                    localFile.getParentFile().mkdirs();
                }
                String workDir = ftpPath.substring(0, ftpPath.lastIndexOf(File.separator));
                if (StringUtil.isEmpty(workDir)) {
                    workDir = "/";
                }
				ftpClient.changeWorkingDirectory(workDir);
				OutputStream os = new FileOutputStream(localFile);
				boolean flag = ftpClient.retrieveFile(fileName, os);
				os.close();
				// ftpClient.logout();
				// ftpClient.disconnect();
				System.out.println("download succes!"+flag+"******"+localFile);
            }
		} catch (FileNotFoundException e) {
			logger.error("没有找到" + ftpPath + "文件");
			e.printStackTrace();
		} catch (SocketException e) {
			logger.error("连接FTP失败.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("文件读取错误。");
			e.printStackTrace();
		}
	}

	/**
     * 遍历 FTP 服务器指定目录下的所有文件(包含子孙文件)
     *
     * @param ftpClient        ：连接成功有效的 FTP客户端连接
     * @param remotePath       ：查询的 FTP 服务器目录，如果文件，则视为无效，使用绝对路径，如"/"、"/video"、"\\"、"\\video"
     * @param relativePathList ：返回查询结果，其中为服务器目录下的文件相对路径，如：\1.png、\docs\overview-tree.html 等
     * @return
     */
    public static List<String> loopServerPath(FTPClient ftpClient, String remotePath, List<String> relativePathList) {
        /**如果 FTP 连接已经关闭，或者连接无效，则直接返回*/
        if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            System.out.println("ftp 连接已经关闭或者连接无效......");
            return relativePathList;
        }
        try {
            /**转移到FTP服务器根目录下的指定子目录
             * 1)"/"：表示用户的根目录，为空时表示不变更
             * 2)参数必须是目录，当是文件时改变路径无效
             * */
            ftpClient.changeWorkingDirectory(remotePath);
            /** listFiles：获取FtpClient连接的当前下的一级文件列表(包括子目录)
             * 1）FTPFile[] ftpFiles = ftpClient.listFiles("/docs/info");
             *      获取服务器指定目录下的子文件列表(包括子目录)，以 FTP 登录用户的根目录为基准，与 FTPClient 当前连接目录无关
             * 2）FTPFile[] ftpFiles = ftpClient.listFiles("/docs/info/springmvc.txt");
             *      获取服务器指定文件，此时如果文件存在时，则 FTPFile[] 大小为 1，就是此文件
             * */
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (FTPFile ftpFile : ftpFiles) {
                    if (ftpFile.isFile()) {
                        String relativeRemotePath = remotePath + "\\" + ftpFile.getName();
                        relativePathList.add(relativeRemotePath);
                    } else {
                        loopServerPath(ftpClient, remotePath + "\\" + ftpFile.getName(), relativePathList);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativePathList;
    }
	/**
     * 遍历 FTP 服务器指定目录下的所有文件(包含子孙文件)
     *
     * @param ftpClient        ：连接成功有效的 FTP客户端连接
     * @param remotePath       ：查询的 FTP 服务器目录，如果文件，则视为无效，使用绝对路径，如"/"、"/video"、"\\"、"\\video"
     * @param relativePathList ：返回查询结果，其中为服务器目录下的文件相对路径，如：\1.png、\docs\overview-tree.html 等
     * @return
     */
    public static void loopServerPath2(FTPClient ftpClient,String localPath, String remotePath) {
        /**如果 FTP 连接已经关闭，或者连接无效，则直接返回*/
        if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            System.out.println("ftp 连接已经关闭或者连接无效......");
            return ;
        }
        try {
            /**转移到FTP服务器根目录下的指定子目录
             * 1)"/"：表示用户的根目录，为空时表示不变更
             * 2)参数必须是目录，当是文件时改变路径无效
             * */
            ftpClient.changeWorkingDirectory(remotePath);
            /** listFiles：获取FtpClient连接的当前下的一级文件列表(包括子目录)
             * 1）FTPFile[] ftpFiles = ftpClient.listFiles("/docs/info");
             *      获取服务器指定目录下的子文件列表(包括子目录)，以 FTP 登录用户的根目录为基准，与 FTPClient 当前连接目录无关
             * 2）FTPFile[] ftpFiles = ftpClient.listFiles("/docs/info/springmvc.txt");
             *      获取服务器指定文件，此时如果文件存在时，则 FTPFile[] 大小为 1，就是此文件
             * */
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if (ftpFiles != null && ftpFiles.length > 0) {
                for (FTPFile ftpFile : ftpFiles) {
                    if (ftpFile.isFile()) {
                        String relativeRemotePath = remotePath + "\\" + ftpFile.getName();
                        downloadFile(ftpClient, localPath, relativeRemotePath);
                    } else {
                        loopServerPath2(ftpClient, localPath,remotePath + "\\" + ftpFile.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }
	/**
	 * 上传文件
	 *
	 * @param ftpHost
	 *            ftp服务器地址
	 * @param ftpUserName
	 *            anonymous匿名用户登录，不需要密码。administrator指定用户登录
	 * @param ftpPassword
	 *            指定用户密码
	 * @param ftpPort
	 *            ftp服务员器端口号
	 * @param ftpPath
	 *            ftp文件存放物理路径
	 * @param fileName
	 *            文件路径
	 * @param input
	 *            文件输入流，即从本地服务器读取文件的IO输入流
	 */
	public static void uploadFile(FTPClient ftp, String ftpPath, String fileName, InputStream input) {
		// FTPClient ftp=null;
		if (!ftp.isConnected() || !ftp.isAvailable()) {
            logger.debug("ftp 连接已经关闭或者连接无效......");
            return ;
        }
		try {
			// ftp=getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
			ftp.changeWorkingDirectory(ftpPath);
			//ftp.setFileType(FTP.BINARY_FILE_TYPE);
			fileName = new String(fileName.getBytes("utf-8"), "iso-8859-1");
			ftp.storeFile(fileName, input);
			input.close();
			// ftp.logout();
			System.out.println("upload succes!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
     * 上传本地文件 或 目录 至 FTP 服务器----保持 FTP 服务器与本地 文件目录结构一致
     *
     * @param ftpClient  连接成功有效的 FTPClinet
     * @param uploadFile 待上传的文件 或 文件夹(此时会遍历逐个上传)
     * @throws Exception
     */
    public static void uploadFiles(FTPClient ftpClient, File uploadFile) {
        /**如果 FTP 连接已经关闭，或者连接无效，则直接返回*/
        if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            System.out.println(">>>>>FTP服务器连接已经关闭或者连接无效*****放弃文件上传****");
            return;
        }
        if (uploadFile == null || !uploadFile.exists()) {
            System.out.println(">>>>>待上传文件为空或者文件不存在*****放弃文件上传****");
            return;
        }
        try {
            if (uploadFile.isDirectory()) {
                /**如果被上传的是目录时
                 * makeDirectory：在 FTP 上创建目录(方法执行完，服务器就会创建好目录，如果目录本身已经存在，则不会再创建)
                 * 1）可以是相对路径，即不以"/"开头，相对的是 FTPClient 当前的工作路径，如 "video"、"视频" 等，会在当前工作目录进行新建目录
                 * 2）可以是绝对路径，即以"/"开头，与 FTPCLient 当前工作目录无关，如 "/images"、"/images/2018"
                 * 3）注意多级目录时，必须确保父目录存在，否则创建失败，
                 *      如 "video/201808"、"/images/2018" ，如果 父目录 video与images不存在，则创建失败
                 * */
                ftpClient.makeDirectory(uploadFile.getName());
                /**变更 FTPClient 工作目录到新目录
                 * 1)不以"/"开头表示相对路径，新目录以当前工作目录为基准，即当前工作目录下不存在此新目录时，变更失败
                 * 2)参数必须是目录，当是文件时改变路径无效*/
                ftpClient.changeWorkingDirectory(uploadFile.getName());

                File[] listFiles = uploadFile.listFiles();
                for (int i = 0; i < listFiles.length; i++) {
                    File loopFile = listFiles[i];
                    if (loopFile.isDirectory()) {
                        /**如果有子目录，则迭代调用方法进行上传*/
                        uploadFiles(ftpClient, loopFile);
                        /**changeToParentDirectory：将 FTPClient 工作目录移到上一层
                         * 这一步细节很关键，子目录上传完成后，必须将工作目录返回上一层，否则容易导致文件上传后，目录不一致
                         * */
                        ftpClient.changeToParentDirectory();
                    } else {
                        /**如果目录中全是文件，则直接上传*/
                        FileInputStream input = new FileInputStream(loopFile);
                        boolean flag = ftpClient.storeFile(loopFile.getName(), input);
                        input.close();
                        System.out.println(flag+">>>>>文件上传成功****" + loopFile.getPath());
                    }
                }
            } else {
                /**如果被上传的是文件时*/
                FileInputStream input = new FileInputStream(uploadFile);
                /** storeFile:将本地文件上传到服务器
                 * 1）如果服务器已经存在此文件，则不会重新覆盖,即不会再重新上传
                 * 2）如果当前连接FTP服务器的用户没有写入的权限，则不会上传成功，但是也不会报错抛异常
                 * */
                boolean flag = ftpClient.storeFile(uploadFile.getName(), input);
                input.close();
                System.out.println(flag+">>>>>文件上传成功****" + uploadFile.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同步本地目录与 FTP 服务器目录
     * 1）约定：FTP 服务器有，而本地没有的，则下载下来；本地有，而ftp服务器没有的，则将本地多余的删除
     * 2）始终确保本地与 ftp 服务器内容一致
     * 2）让 FTP 服务器与 本地目录保持结构一致，如 服务器上是 /docs/overview-tree.html，则本地也是 localDir/docs/overview-tree.html
     *
     * @param ftpClient        连接成功有效的 FTPClinet
     * @param localSyncFileDir ：与 FTP 目录进行同步的本地目录
     */
    public static void syncLocalDir(FTPClient ftpClient, String localSyncFileDir) throws IOException {
        /**如果 FTP 连接已经关闭，或者连接无效，则直接返回*/
        if (!ftpClient.isConnected() || !ftpClient.isAvailable() || StringUtil.isEmpty(localSyncFileDir)) {
            System.out.println(">>>>>FTP服务器连接已经关闭或者连接无效*********");
            return;
        }

        /** 获取本地存储目录下的文件*/
        Collection<File> fileCollection = localListFiles(new File(localSyncFileDir));
        System.out.println(">>>>>本地存储目录共有文件数量*********" + fileCollection.size());

        /**获取 FTP 服务器下的相对路径*/
        List<String> relativePathList = new ArrayList<>();
        relativePathList = loopServerPath(ftpClient, "", relativePathList);
        System.out.println(">>>>>FTP 服务器端共有文件数量*********" + relativePathList.size());

        /**
         * 遍历 本地存储目录下的文件
         * 1）如果本地文件在 FTP 服务器上不存在，则删除它
         * 2）如果本地文件在 FTP 服务器上存在，则比较两种大小
         *    如果大小不一致，则重新下载
         */
        for (File localFile : fileCollection) {
            String localFilePath = localFile.getPath();
            String localFileSuffi = localFilePath.replace(localSyncFileDir, "");
            if (relativePathList.contains(localFileSuffi)) {
                /**本地此文件在 FTP 服务器存在
                 * 1)比较大小，如果本地文件与服务器文件大小一致，则跳过
                 * 2）如果大小不一致，则删除本地文件，重新下载
                 * 3）最后都要删除relativePathList中的此元素，减轻后一次循环的压力*/
                FTPFile[] ftpFiles = ftpClient.listFiles(localFileSuffi);
                System.out.println(">>>>>本地文件 在 FTP 服务器已存在*********" + localFile.getPath());
                if (ftpFiles.length >= 1 && localFile.length() != ftpFiles[0].getSize()) {
                	downloadFile(ftpClient, localSyncFileDir, localFileSuffi);
                    System.out.println(">>>>>本地文件与 FTP 服务器文件大小不一致，重新下载*********" + localFile.getPath());
                }
                relativePathList.remove(localFileSuffi);
            } else {
                System.out.println(">>>>>本地文件在 FTP 服务器不存在，删除本地文件*********" + localFile.getPath());
                /**本地此文件在 FTP 服务器不存在
                 * 1)删除本地文件
                 * 2）如果当前文件所在目录下文件已经为空，则将此父目录也一并删除*/
                localFile.delete();
                File parentFile = localFile.getParentFile();
                while (parentFile.list().length == 0) {
                    parentFile.delete();
                    parentFile = parentFile.getParentFile();
                }
            }
        }
        for (int i = 0; i < relativePathList.size(); i++) {
            System.out.println(">>>>> FTP 服务器存在新文件，准备下载*********" + relativePathList.get(i));
            downloadFile(ftpClient, localSyncFileDir, relativePathList.get(i));
        }
    }
    /**
     * 删除服务器的文件
     *
     * @param ftpClient   连接成功且有效的 FTP客户端
     * @param deleteFiles 待删除的文件或者目录，为目录时，会逐个删除，
     *                    路径必须是绝对路径，如 "/1.png"、"/video/3.mp4"、"/images/2018"
     *                    "/" 表示用户根目录,则删除所有内容
     */
    public static void deleteServerFiles(FTPClient ftpClient, String deleteFiles) {
        /**如果 FTP 连接已经关闭，或者连接无效，则直接返回*/
        if (!ftpClient.isConnected() || !ftpClient.isAvailable()) {
            System.out.println(">>>>>FTP服务器连接已经关闭或者连接无效*****放弃文件上传****");
            return;
        }
        try {
            /** 尝试改变当前工作目录到 deleteFiles
             * 1）changeWorkingDirectory：变更FTPClient当前工作目录，变更成功返回true，否则失败返回false
             * 2）如果变更工作目录成功，则表示 deleteFiles 为服务器已经存在的目录
             * 3）否则变更失败，则认为 deleteFiles 是文件，是文件时则直接删除
             */
            boolean changeFlag = ftpClient.changeWorkingDirectory(deleteFiles);
            if (changeFlag) {
                /**当被删除的是目录时*/
                FTPFile[] ftpFiles = ftpClient.listFiles();
                for (FTPFile ftpFile : ftpFiles) {
                    System.out.println("----------------::::" + ftpClient.printWorkingDirectory());
                    if (ftpFile.isFile()) {
                        boolean deleteFlag = ftpClient.deleteFile(ftpFile.getName());
                        if (deleteFlag) {
                            System.out.println(">>>>>删除服务器文件成功****" + ftpFile.getName());
                        } else {
                            System.out.println(">>>>>删除服务器文件失败****" + ftpFile.getName());
                        }
                    } else {
                        /**printWorkingDirectory：获取 FTPClient 客户端当前工作目录
                         * 然后开始迭代删除子目录
                         */
                        String workingDirectory = ftpClient.printWorkingDirectory();
                        deleteServerFiles(ftpClient, workingDirectory + "/" + ftpFile.getName());
                    }
                }
                /**printWorkingDirectory：获取 FTPClient 客户端当前工作目录
                 * removeDirectory：删除FTP服务端的空目录，注意如果目录下存在子文件或者子目录，则删除失败
                 * 运行到这里表示目录下的内容已经删除完毕，此时再删除当前的为空的目录，同时将工作目录移动到上移层级
                 * */
                String workingDirectory = ftpClient.printWorkingDirectory();
                ftpClient.removeDirectory(workingDirectory);
                ftpClient.changeToParentDirectory();
            } else {
                /**deleteFile：删除FTP服务器上的文件
                 * 1）只用于删除文件而不是目录，删除成功时，返回 true
                 * 2）删除目录时无效,方法返回 false
                 * 3）待删除文件不存在时，删除失败，返回 false
                 * */
                boolean deleteFlag = ftpClient.deleteFile(deleteFiles);
                if (deleteFlag) {
                    System.out.println(">>>>>删除服务器文件成功****" + deleteFiles);
                } else {
                    System.out.println(">>>>>删除服务器文件失败****" + deleteFiles);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 遍历目录下的所有文件--方式1
     *
     * @param targetDir
     */
    public static Collection<File> localListFiles(File targetDir) {
        Collection<File> fileCollection = new ArrayList<>();
        if (targetDir != null && targetDir.exists() && targetDir.isDirectory()) {
            /**
             * targetDir：不要为 null、不要是文件、不要不存在
             * 第二个 文件过滤 参数如果为 FalseFileFilter.FALSE ，则不会查询任何文件
             * 第三个 目录过滤 参数如果为 FalseFileFilter.FALSE , 则只获取目标文件夹下的一级文件，而不会迭代获取子文件夹下的文件
             */
            fileCollection = FileUtils.listFiles(targetDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        }
        return fileCollection;
    }

	/**
     * 使用完毕，应该及时关闭连接
     * 终止 ftp 传输
     * 断开 ftp 连接
     *
     * @param ftpClient
     * @return
     */
    public static FTPClient closeFTPConnect(FTPClient ftpClient) {
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.abort();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient;
    }

	/*
	 * static String ftpHost= "61.145.228.3"; //ftp服务器地址 static int ftpPort =
	 * 30080;//ftp服务员器端口号 static String ftpUserName =
	 * "DXPENT0000027470";//anonymous匿名用户登录，不需要密码。administrator指定用户登录 static
	 * String ftpPassword = "DxpYtzh@0753";//指定用户密码 static String ftpPath =
	 * "57000001"; //ftp文件存放物理路径 static String filePath=""; //文件路径 static String
	 * fileName="";//文件名称
	 */ static String ftpHost = "192.168.16.108"; // ftp服务器地址
	static int ftpPort = 21;// ftp服务员器端口号
	static String ftpUserName = "test";// anonymous匿名用户登录，不需要密码。administrator指定用户登录
	static String ftpPassword = "123456";// 指定用户密码
	static String ftpPath = "57000001"; // ftp文件存放物理路径
	static String filePath = ""; // 文件路径
	static String fileName = "";// 文件名称

	public static void main(String[] args) throws Exception {
		FTPClient ftp = getFTPClient(ftpHost, ftpUserName, ftpPassword, ftpPort);
		filePath = "E:"+File.separator+"wmsResult"+File.separator+"test1";
		String filePath2 = "E:"+File.separator+"wmsResult"+File.separator+"test1";
		fileName = "131.txt";
		String ftpPath = File.separator + "test";
		//String str = BuildMessageUtil.buildHzStr(null, null);
		File file = new File(filePath);
		/*FileOutputStream fos = new FileOutputStream(file);
		byte[] b = str.getBytes("utf-8");
		fos.write(b);
		fos.close();
		FileInputStream input = new FileInputStream(new File(filePath + File.separatorChar + fileName));*/
		//FtpUtil.uploadFile(ftp,  filePath2,filePath2 + File.separator + fileName, input);
		FtpUtil.uploadFiles(ftp, file);
		FtpUtil.downloadFile(ftp, filePath2, ftpPath);
		//FtpUtil.loopServerPath2(ftp, filePath,ftpPath);
		ftp.disconnect();
	}

}
