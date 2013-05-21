package util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;

public class XMPPConnUtil {

	private static XMPPConnection connection;

	public static synchronized XMPPConnection getConnection() {
		if (connection != null) {
			return connection;
		} else {

			ConnectionConfiguration config = new ConnectionConfiguration(
					"192.168.1.105", 5222);
			/** 是否启用安全验证 */
			config.setSASLAuthenticationEnabled(false);

			config.setSecurityMode(SecurityMode.required);
			config.setCompressionEnabled(false);
			/** 是否启用调试 */
			// config.setDebuggerEnabled(true);
			/** 创建connection链接 */
			connection = new XMPPConnection(config);

		}

		return connection;

	}
}
