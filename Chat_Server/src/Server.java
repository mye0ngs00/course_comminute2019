import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/* ��ü�� ����, ����. ������ �����ϰ� ������û�� ���� ���, ä�ñ�� ���� */
public class Server {
	private static final int PORT = 4000;
	private static ServerSocket serverSocket = null;
	private static ServerSocket serverSocket_connection = null;
	private	static ServerBroadcast serverBroadcast = null;
	private Member member = null;
	
	// ����DB ����
	private static final int LOG_PORT = 4006;
	private static ServerSocket chatSocket = null;
	private static BufferedReader br_chatSocket = null;
	private static BufferedWriter bw_chatSocket =null;
	
	// ����/����
	private static final int ADMISSION_PORT = 4002;
	
	public static void main(String[] args) {
		member = new Member();
		BufferedReader br;
		new ServerDB();
		
		// ä�ñ�� ����
		new Thread(()->{
			try {
				chatSocket = new ServerSocket(LOG_PORT);
				Socket c_soc;
				c_soc = chatSocket.accept();
				br_chatSocket = new BufferedReader(new InputStreamReader(c_soc.getInputStream()));
				bw_chatSocket = new BufferedWriter(new OutputStreamWriter(c_soc.getOutputStream()));
				
				String msgTmp = br_chatSocket.readLine();
				String[] msgTmpAr = msgTmp.split("@");
				ServerDB.rs = ServerDB.st.executeQuery("select * from chatlog");
				
				// �������� ���� ä�� ��� ����
				boolean checkPoint = true;
				while(ServerDB.rs.next()) {
					if(checkPoint == false) {
						bw_chatSocket.write(ServerDB.rs.getString("time") + "@" + ServerDB.rs.getString("sender_id") + "@" + ServerDB.rs.getString("course_id") + "@" + ServerDB.rs.getString("payload"));
						bw_chatSocket.flush();
					}
					else if(ServerDB.rs.getString("time").equals(msgTmpAr[0])) continue;
					else checkPoint = false;
				}
				
				// ���� ������
				bw_chatSocket.write("-1");
				bw_chatSocket.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		
		try {
			System.out.println("Comunication Server Start");
			serverSocket = new ServerSocket(PORT);
			serverBroadcast = new ServerBroadcast(member);
			
			// ��/���� ���� ����
			serverSocket_connection = new ServerSocket(ADMISSION_PORT);
			
			// ����-Ŭ���̾�Ʈ�� ä��
			Thread threadBroadcast = new Thread(serverBroadcast);
			threadBroadcast.start();
			
			
			// �������� ����� ������
			while (true) {
				// ���ο� ������ ����
				Socket socket = serverSocket.accept();
				Socket socket_con = serverSocket_connection.accept();
				br= new BufferedReader(new InputStreamReader(socket_con.getInputStream(), "UTF-8"));
				
				// ����/����
				String userId = br.readLine();
				member.addMember(socket, userId);
				System.out.println(socket.getInetAddress() + " connection complete...");
				
				// ���� ����� + ��ε�ĳ���� ����
				Thread thread = new Thread(new ServerReceiver(socket, member));
				thread.start();
			}
		} catch (IOException e) {
			e1.printStackTrace();
		}
	}
} 