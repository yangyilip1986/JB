package com.jiubang.p2p.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jiubang.p2p.AppConstants;
import com.jiubang.p2p.AppVariables;

public class HttpHelper {

	public Bitmap getCapture(String sid) {
		InputStream is = null;
		try {
			// 创建连接
			URL url = new URL(AppConstants.CAPTCHA);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();
			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			JSONObject obj = new JSONObject();
			obj.put("sid", sid);
			out.writeBytes(obj.toString());
			out.flush();
			out.close();
			// 读取响应
			is = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			// 断开连接
			connection.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public Bitmap sendToService(String st) {
		InputStream is = null;
		try {
			// 创建连接
			URL url = new URL(AppConstants.CAPTCHA);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();
			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			JSONObject obj = new JSONObject();
			obj.put("log", st);
			out.writeBytes(obj.toString());
			out.flush();
			out.close();
			// 读取响应
			is = connection.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			// 断开连接
			connection.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public JSONObject getNewVerson(String sid) {
		InputStream is = null;
		try {
			// 创建连接
			URL url = new URL(AppConstants.CAPTCHA);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();
			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			JSONObject obj = new JSONObject();
			obj.put("sid", sid);
			out.writeBytes(obj.toString());
			out.flush();
			out.close();
			// 读取响应
			BufferedReader reader = null;
			StringBuilder respond = new StringBuilder();
			is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			int len = 0;
			char[] buf = new char[1024];
			while ((len = reader.read(buf)) != -1) {
				respond.append(buf, 0, len);
			}
			String s = inputStream2String(is);
			JSONObject jo = new JSONObject(s);
			// 断开连接
			connection.disconnect();
			return jo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public JSONObject getSlideView() {
		InputStream is = null;
		try {
			// 创建连接
			URL url = new URL(AppConstants.GET_SLIDE_IMAGE);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.connect();
			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			JSONObject obj = new JSONObject();
			obj.put("sid", AppVariables.sid);
			out.writeBytes(obj.toString());
			out.flush();
			out.close();
			// 读取响应
//			BufferedReader reader = null;
//			StringBuilder respond = new StringBuilder();
			is = connection.getInputStream();
			String s = inputStream2String(is);
			JSONObject jo = new JSONObject(s);
			// 断开连接
			connection.disconnect();
			return jo;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

//	/**
//	 * 将InputStream转换成某种字符编码的String
//	 *
//	 * @param in
//	 * @return
//	 * @throws Exception
//	 */
//	private String InputStreamTOString(InputStream in) throws Exception {
//
//		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//		byte[] data = new byte[1024];
//		int count = -1;
//		while ((count = in.read(data, 0, 1024)) != -1)
//			outStream.write(data, 0, count);
//		data = null;
//		return new String(outStream.toByteArray(), "UTF-8");
//	}

	public static String inputStream2String(InputStream in) throws IOException {

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		int n;
		while ((n = in.read(b)) != -1) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

}
