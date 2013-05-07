package activity.chat;

import java.io.File;

public class config {
	public static String SDCARD = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static File path = new File(SDCARD + "/RunChatDatabase/"); // 数据库文件目录
	public static File f = new File(SDCARD + "/RunChatDatabase/config.db"); // 数据库文件
}