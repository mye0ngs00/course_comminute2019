/*서버에서 요청을 받아오고 Thread 시키는 동작을 시키며
 *  각 Thread 에 생성된 Member 객체를 
 *  매개변수로 넣어주며 같은 객체를 통합 관리할수 있는 시스템을 
 *  구축한다.*/
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
				//선형탐색해서 이전/이후로 나누기
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
			//멤버들을 broadcast 대상에 추가
			serverBroadcast = new ServerBroadcast(member);
			serverSocket_connection = new ServerSocket(PORT+2);
			
			//멀티스레드로 broadcast.
			Thread threadBroadcast = new Thread(serverBroadcast);
			threadBroadcast.start();
			
			
			//서버소켓 연결시 멤버등록
			while (true) {
				Socket socket = serverSocket.accept();
				Socket socket_con = serverSocket_connection.accept();
				//TODO: 
				/* 굳이 socket을 하나 더 뚫은 이유는 입/퇴장을 담당하는 포트이기 때문 */
				br= new BufferedReader(new InputStreamReader(socket_con.getInputStream(), "UTF-8"));
				//4002번 포트로 기다림.
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