package com.mingrisoft.webserver.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import android.util.Log;

public class HttpFileHandler implements HttpRequestHandler {

	private String webRoot;

	public HttpFileHandler(final String webRoot) {
		this.webRoot = webRoot;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException {

		String target = URLDecoder.decode(request.getRequestLine().getUri(),
				"UTF-8");
		final File file = new File(this.webRoot, target);

		if (!file.exists()) { // 不存在
			response.setStatusCode(HttpStatus.SC_NOT_FOUND);//404
			StringEntity entity = new StringEntity(
					"<html><body><h1>Error 404, file not found.</h1></body></html>",
					"UTF-8");
			response.setHeader("Content-Type", "text/html");
			response.setEntity(entity);
		} else if (file.canRead()) { // 可读
			response.setStatusCode(HttpStatus.SC_OK);//SC_OK 200
			HttpEntity entity = null;
			if (file.isDirectory()) { // 文件夹
				entity = createDirListHtml(file, target);
				response.setHeader("Content-Type", "text/html");
			} else { // 文件
				String contentType = URLConnection
						.guessContentTypeFromName(file.getAbsolutePath());
				contentType = null == contentType ? "charset=UTF-8"
						: contentType + "; charset=UTF-8";
				entity = new FileEntity(file, contentType);
				response.setHeader("Content-Type", contentType);
			}
			response.setEntity(entity);
		} else { // 不可读
			response.setStatusCode(HttpStatus.SC_FORBIDDEN);
			StringEntity entity = new StringEntity(
					"<html><body><h1>Error 403,拒绝访问 看看程序哪错误了.</h1></body></html>",
					"UTF-8");
			response.setHeader("Content-Type", "text/html");
			response.setEntity(entity);
		}
	}

	/** 创建文件列表浏览网页 */
	private StringEntity createDirListHtml(File dir, String target)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append("<!DOCTYPE HTML>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n<title>");
		sb.append(null == target ? dir.getAbsolutePath() : target);
		sb.append(" 的索引</title>\n");
		sb.append("<link rel=\"stylesheet\" href=\"/.wfs/css/examples.css\" type=\"text/css\"/>\n");
		sb.append("<link rel=\"stylesheet\" href=\"/.wfs/css/wifi.css\" type=\"text/css\"/>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/jquery-1.8.3.min.js\" ></script>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/handlers.js\" ></script>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/swfupload.js\"></script>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/fileprogress.js\"></script>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/swfupload.queue.js\" ></script>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/main.js\" ></script>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/wsf.js\" ></script>\n");
		sb.append("<script  type=\"text/javascript\" src=\"/.wfs/js/jquery-impromptu.4.0.min.js\"></script>\n");
		
		sb.append("</head>\n<body onload=\"loadDir();\">\n");
		
		sb.append("<div class='topDiv'>\n");
		sb.append("<center>\n");
		sb.append("<div class='topbox'>\n");
		sb.append("<div class='topright'>\n");
		sb.append("<span  id=\"spanButtonPlaceholder\"></span>\n");
		sb.append("</div>\n");
		sb.append("<div class='toprightx'>查看手机文件夹,OSSHUHAI/WIFI中的文件</div>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		
		sb.append("</center>\n");
		sb.append("</div>\n");
		sb.append("<center>\n");
		sb.append("<div class='cen'>以下是你设备上的文件列表：</div>\n");
		sb.append("<div class='show_title'>\n<div class='show_title1'>");
		sb.append("<table class='bt'>\n");
		sb.append("<tr>\n");
		sb.append("<td style='width: 500px; text-align: left; padding-left: 60px;'>文件名称</td>\n");
		sb.append("<td style='width: 145px;'>大小</td>\n");
		sb.append("<td style='width: 140px;'>操作</td>\n");
		sb.append("</tr>\n");
		sb.append("</table>\n");
		sb.append("</div>\n");
		sb.append("</div>\n");
		sb.append("<div class='show_body'>\n");
		sb.append("<table class='bt1'>\n");
		
		/* 目录列表 */
		File[] files = dir.listFiles();
		if (null != files) {
			sort(files); // 排序
			for (File f : files) {
				if(f.getName().trim().endsWith(".txt")){
					appendRow(sb, f);
				}
			}
		}
		sb.append("</table>\n");
		sb.append("</div>\n");
		sb.append("</center>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");
		
		//sb.append("</table>\n<hr noshade>\n<em>Welcome to <a target=\"_blank\" href=\"http://vaero.blog.51cto.com/\">winorlose2000's blog</a>!</em>\n</body>\n</html>");
		return new StringEntity(sb.toString(), "UTF-8");
	}

	private boolean isSamePath(String a, String b) {
		String left = a.substring(b.length(), a.length()); // a以b开头
		if (left.length() >= 2) {
			return false;
		}
		if (left.length() == 1 && !left.equals("/")) {
			return false;
		}
		return true;
	}

	/** 排序：文件夹、文件，再各安字符顺序 */
	private void sort(File[] files) {
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File f1, File f2) {
				if (f1.isDirectory() && !f2.isDirectory()) {
					return -1;
				} else if (!f1.isDirectory() && f2.isDirectory()) {
					return 1;
				} else {
					return f1.toString().compareToIgnoreCase(f2.toString());
				}
			}
		});
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd ahh:mm");

	private void appendRow(StringBuffer sb, File f) {
		String clazz, link, size;
		if (f.isDirectory()) {
			clazz = "icon dir";
			link = f.getName() + "/";
			size = "";
		} else {
			clazz = "icon file";
			link = f.getName();
			size = formatFileSize(f.length());
			sb.append("<tr>\n");
			
			sb.append("<td style='width: 500px; text-align: left; padding-left: 60px;'>"+link+"</td>\n");
			sb.append("<td style='width: 145px;'>"+size+"</td>\n");
			//sb.append("<td style='width: 141px;'><a class=\"icon up\" href=\"..\">下载</a>&nbsp;|&nbsp;<a href=\"..\">删除</a></td>\n");
			sb.append("<td style='width: 140px;'>");
			sb.append("<a class=\"icon up\" href=\"");
			sb.append(link);
			sb.append(WebServer.SUFFIX_ZIP);
			sb.append("\">下载</a>&nbsp;|&nbsp;");
			//if (f.canWrite() && !hasWfsDir(f)) {
				Log.e("", "-----------------------------");
				sb.append("<a href=\"");
			//	sb.append(link);
			//	sb.append(WebServer.SUFFIX_DEL);
				sb.append("\" onclick=\"return confirmDelete('");
				sb.append(link);
				sb.append(WebServer.SUFFIX_DEL);
				sb.append("')\">删除</a>");
			//}
			sb.append("</td>\n</tr>\n");
		}
	}

	public static boolean hasWfsDir(File f) {
		String path = f.isDirectory() ? f.getAbsolutePath() + "/" : f
				.getAbsolutePath();
		return path.indexOf("/.wfs/") != -1;
	}

	/** 获得文件大小表示 */
	private String formatFileSize(long len) {
		if (len < 1024)
			return len + " B";
		else if (len < 1024 * 1024)
			return len / 1024 + "." + (len % 1024 / 10 % 100) + " KB";
		else if (len < 1024 * 1024 * 1024)
			return len / (1024 * 1024) + "." + len % (1024 * 1024) / 10 % 100
					+ " MB";
		else
			return len / (1024 * 1024 * 1024) + "." + len
					% (1024 * 1024 * 1024) / 10 % 100 + " MB";
	}

}
