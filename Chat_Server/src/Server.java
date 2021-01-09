/*�������� ��û�� �޾ƿ��� Thread ��Ű�� ������ ��Ű��
 *  �� Thread �� ������ Member ��ü�� 
 *  �Ű������� �־��ָ� ���� ��ü�� ���� �����Ҽ� �ִ� �ý����� 
 *  �����Ѵ�.*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	final static int PORT = 4000;
	private static ServerSocket serverSocket = null;
	private static ServerSocket serverSocket_connection = null;
	private	static ServerBroadcast serverBroadcast = null;
	private Member member = null;
	
	//local-db correction
	private static ServerSocket chatSocket = null;
	private static BufferedReader br_chatSocket = null;
	private static BufferedWriter bw_chatSocket =null;
	
	public static void main(String[] args) {
		Member member = new Member();
		BufferedReader br;
		new ServerDB();
		
		new Thread(()->{
			try {
				chatSocket = new ServerSocket(4006);
				Socket c_soc;
				c_soc = chatSocket.accept();
				br_chatSocket = new BufferedReader(new InputStreamReader(c_soc.getInputStream()));
				bw_chatSocket = new BufferedWriter(new OutputStreamWriter(c_soc.getOutputStream()));
				
				String msgTmp = br_chatSocket.readLine();
				String[] msgTmpAr = msgTmp.split("@");
				ServerDB.rs = ServerDB.st.executeQuery("select * from chatlog");
				
				boolean checkPoint = true;
				//����Ž���ؼ� ����/���ķ� ������
				while(ServerDB.rs.next()) {
					if(checkPoint == false) {
						bw_chatSocket.write(ServerDB.rs.getString("time") + "@" + ServerDB.rs.getString("sender_id") + "@" + ServerDB.rs.getString("course_id") + "@" + ServerDB.rs.getString("payload"));
						bw_chatSocket.flush();
					}
					else if(ServerDB.rs.getString("time").equals(msgTmpAr[0])) continue;
					else checkPoint = false;
				}
				bw_chatSocket.write("-1");
				bw_chatSocket.flush();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}).start();
		
		try {
			//course community server start.
			System.out.println("Comunication Server Start");
			serverSocket = new ServerSocket(PORT);
			//������� broadcast ��� �߰�
			serverBroadcast = new ServerBroadcast(member);
			serverSocket_connection = new ServerSocket(PORT+2);
			
			//��Ƽ������� broadcast.
			Thread threadBroadcast = new Thread(serverBroadcast);
			threadBroadcast.start();
			
			
			//�������� ����� ������
			while (true) {
				Socket socket = serverSocket.accept();
				Socket socket_con = serverSocket_connection.accept();
				//TODO: 
				/* ���� socket�� �ϳ� �� ���� ������ ��/������ ����ϴ� ��Ʈ�̱� ���� */
				br= new BufferedReader(new InputStreamReader(socket_con.getInputStream(), "UTF-8"));
				//4002�� ��Ʈ�� ��ٸ�.
				String userId =br.readLine();
				member.addMember(socket, userId);
				System.out.println(socket.getInetAddress() + " connection complete...");
				
				Thread thread = new Thread(new ServerReceiver(socket, member));
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Server OFF");
		}
	}
} 