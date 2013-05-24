package activity.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collection;

import notification.client.Constants;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import util.LoginUtil;
import util.XMPPConnUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ClientConServer {
	private String ipstring = null, nameString = null;
	Thread thread = null;
	Socket s = null;
	private InetSocketAddress isa = null;
	DataInputStream dis = null;
	DataOutputStream dos = null;
	private String reMsg = null;
	private String chatKey = "chatKey";
	private String myName = LoginUtil.USERNAME;
	private boolean isConnect = false;
	private static int PORT = 5222;
	private Context context;

	public ClientConServer(Context context) {
		this.context = context;

	}

	private Runnable doThread = new Runnable() {
		public void run() {
			System.out.println("running!");
			ReceiveMsg();
		}
	};

	public void connect() {
		thread = new Thread(null, connThread, "connect");
		thread.start();
	}

	private Runnable connThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				s = new Socket();
				isa = new InetSocketAddress(Constants.IP, 1235);
				s.connect(isa, 5000);

				if (s.isConnected()) {
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
					dos.writeUTF(chatKey + "online:" + myName);
					/**
					 * 这里是关键，我在此耗时8h+ 原因是 子线程不能直接更新UI 为此，我们需要通过Handler物件，通知主线程Ui
					 * Thread来更新界面。
					 * 
					 */
					thread = new Thread(null, doThread, "Message");
					thread.start();
					System.out.println("connect");
					isConnect = true;
				}
			} catch (UnknownHostException e) {
				System.out.println("連接失敗");
				// Toast.makeText(SocketmsgActivity.this, "連接失敗",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new
				// Intent(SocketmsgActivity.this,IniActivity.class);
				// startActivity(intent);
				// SocketmsgActivity.this.finish();
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				System.out.println("連接超時，服務器未開啟或IP錯誤");
				// Toast.makeText(SocketmsgActivity.this, "連接超時，服務器未開啟或IP錯誤",
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new
				// Intent(SocketmsgActivity.this,IniActivity.class);
				// startActivity(intent);
				// SocketmsgActivity.this.finish();
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("連接失敗");
				e.printStackTrace();
			}
		}
	};

	public void disConnect() {
		if (dos != null) {
			try {
				dos.writeUTF(chatKey + "offline:" + myName);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 线程监视Server信息
	 */
	private void ReceiveMsg() {
		if (isConnect) {
			try {
				while ((reMsg = dis.readUTF()) != null) {
					System.out.println(reMsg);
					if (reMsg != null) {

						try {
							android.os.Message msgMessage = new android.os.Message();
							msgMessage.what = 0x1981;
							handler.sendMessage(msgMessage);
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			} catch (SocketException e) {
				// TODO: handle exception
				System.out.println("exit!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * 通过handler更新UI
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x1981:

				// ChatMsgEntity entity = new ChatMsgEntity();
				// entity.setDate(getDate());
				// entity.setName(toName);
				// entity.setMsgType(true);
				// entity.setText(reMsg);
				Intent intent = new Intent("chat.message");
				intent.putExtra("reMsg", reMsg);
				context.sendBroadcast(intent);
				break;
			}
		}
	};

	// 这里收到消息后，通过广播将消息发送到需要的地方.哈哈，既然收到了服务器发送来的信息，如何处理自己决定。
	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message m) {
	// Message msg = new Message();
	// msg = (Message) m.obj;
	// // 把从服务器获得的消息通过广播发送
	// Intent intent = new Intent("chat.message");
	// String[] message = new String[] { msg.getFrom(), msg.getBody() };
	// System.out.println("==========收到服务器消息  From==========="
	// + message[0].toString());
	// System.out.println("==========收到服务器消息  Body==========="
	// + message[1].toString());
	// intent.putExtra("message", message);
	// context.sendBroadcast(intent);
	// };
	// };

	// public boolean login(String a, String p) {
	// // ConnectionConfiguration config = new
	// // ConnectionConfiguration("192.168.0.124", PORT);
	// /** 是否启用安全验证 */
	// // config.setSASLAuthenticationEnabled(false);
	// /** 是否启用调试 */
	// // config.setDebuggerEnabled(true);
	// /** 创建connection链接 */
	// // XMPPConnection connection = new XMPPConnection(config);
	// XMPPConnection connection = XMPPConnUtil.getConnection();
	// try {
	// /** 建立连接 */
	// connection.connect();
	//
	// /** 登录 */
	// connection.login(a, p);
	// /** 开启读写线程，并加入到管理类中 */
	// // ClientSendThread cst=new ClientSendThread(connection);
	// // cst.start();
	// // ManageClientThread.addClientSendThread(a, cst);
	//
	// // 获取用户组、成员信息。
	// System.out.println("======开始获取组及用户==========");
	// Roster roster = connection.getRoster();
	// Collection<RosterGroup> entriesGroup = roster.getGroups();
	// System.out.println("组的个数：" + entriesGroup.size());
	// for (RosterGroup group : entriesGroup) {
	// Collection<RosterEntry> entries = group.getEntries();
	// System.out.println("=========groupName===" + group.getName());
	// for (RosterEntry entry : entries) {
	// // Presence presence = roster.getPresence(entry.getUser());
	// // Log.i("---", "user: "+entry.getUser());
	// System.out.println("组成员的名字：" + entry.getName());
	// System.out.println("组成员的user：" + entry.getUser());
	//
	// // Log.i("---", "tyep: "+entry.getType());
	// // Log.i("---", "status: "+entry.getStatus());
	// // Log.i("---", "groups: "+entry.getGroups());
	// }
	// }
	// System.out.println("======结束获取组及用户==========");
	//
	// // 在登陆以后应该建立一个监听消息的监听器，用来监听收到的消息：
	// ChatManager chatManager = connection.getChatManager();
	// chatManager.addChatListener(new MyChatManagerListener());
	//
	// return true;
	// } catch (XMPPException e) {
	// e.printStackTrace();
	// }
	// return false;
	// }

	// /** message listener */
	// class MyChatManagerListener implements ChatManagerListener {
	//
	// public void chatCreated(Chat chat, boolean arg1) {
	// chat.addMessageListener(new MessageListener() {
	// public void processMessage(Chat arg0, Message msg) {
	// /** 通过handler转发消息 */
	// android.os.Message m = handler.obtainMessage();
	// m.obj = msg;
	// m.sendToTarget();
	//
	// }
	// });
	// }
	// }
}
