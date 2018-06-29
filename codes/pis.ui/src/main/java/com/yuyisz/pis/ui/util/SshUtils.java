package com.yuyisz.pis.ui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshUtils {

	private static JSch jsch;
	private static Session session;
	private static ChannelExec ce;

	/**
	 * @function connect
	 * @param user
	 *            : linux机器登录用户名
	 * @param host
	 *            : linux机器ip地址
	 * @param pwd
	 *            : linux机器登录用户密码
	 * @Description 开启ssh连接，登录至指定linux机器中
	 * **/
	public static boolean connect(String user, String host, String pwd) {
		boolean status = false;
		if (ping(host)) {
			jsch = new JSch();
			try {
				// 通过jsch获取会话，ssh端口号默认为22
				session = jsch.getSession(user, host, 22);
				// 输入用户密码
				session.setPassword(pwd);
				// 创建配置文件，修改会话的密钥验证模式
				Properties config = new Properties();
				config.put("StrictHostKeyChecking", "no");
				session.setConfig(config);
				// 开启会话连接
				session.connect();
				// 通过会话开启执行通道
				ce = (ChannelExec) session.openChannel("exec");
				status = true;
			} catch (JSchException e) {
				// e.printStackTrace();
			}
		}
		return status;
	}

	/**
	 * @function disconnect
	 * @Description 关闭连接资源
	 * **/
	public static void disconnect() {
		if (session != null)
			session.disconnect();
		if (ce != null)
			ce.disconnect();
	}

	/**
	 * @function execCmd
	 * @param command
	 *            : 所要执行的命令
	 * @param user
	 *            : linux机器登录用户名
	 * @param host
	 *            : linux机器ip地址
	 * @param pwd
	 *            : linux机器登录用户密码
	 * @Description 开启ssh连接，登录至指定linux机器中执行指定命令后返回执行结果
	 * @return Object[]
	 * @date 2017/11/16
	 *       修改返回值，由String改为Object[]，Object[]下标0为执行状态true成功，false失败；下标1为执行结果
	 * **/
	public static String execCmd(String command, String user, String host,
			String pwd) {
		String res = null;
		try {
			if (connect(user, host, pwd)) {
				try {
					// 获取执行通道输入流
					InputStream in = ce.getInputStream();
					// 执行指定命令
					ce.setCommand(command);
					ce.setErrStream(System.err);
					// 开启通道连接
					ce.connect();
					// 从输入流中获取命令执行结果
					res = IOUtils.toString(in, "UTF-8").trim();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (JSchException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return res;
	}

	/**
	 * @function ping
	 * @param ip
	 *            : 需要测试的ip
	 * @Description ping指定ip查看是否互通，并返回结果
	 * @return boolean
	 * **/
	public static boolean ping(String ip) {
		boolean status = false;
		if (ip != null && !"".equals(ip)) {
			try {
				status = InetAddress.getByName(ip).isReachable(3000);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return status;
	}
	
	public static void sshSftp(String ip, String user, String pwd, String dir,
			String file, String path) throws Exception {
		Session session = null;
		Channel channel = null;
		int port = 22;
		JSch jsch = new JSch();
		if (port <= 0) {
			// 连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		} else {
			// 采用指定的端口连接服务器
			session = jsch.getSession(user, ip, port);
		}

		// 如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}

		// 设置登陆主机的密码
		session.setPassword(pwd);// 设置密码
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");
		// 设置登陆超时时间
		session.connect(30000);

		try {
			// 创建sftp通信通道
			channel = (Channel) session.openChannel("sftp");
			channel.connect(1000);
			ChannelSftp sftp = (ChannelSftp) channel;

			// 进入服务器指定的文件夹
			sftp.cd(dir);

			// 以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
			// OutputStream outstream = sftp.put("1.txt");
			// InputStream instream = new FileInputStream(new
			// File("c:/print.txt"));
			OutputStream outstream = new FileOutputStream(new File(path + file));
			InputStream instream = sftp.get(file);

			byte b[] = new byte[1024];
			int n;
			while ((n = instream.read(b)) != -1) {
				outstream.write(b, 0, n);
			}

			outstream.flush();
			outstream.close();
			instream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}
}
