import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class main_server {
	private static ServerSocket id_serverSocket = null;
	private static ServerSocket pw_serverSocket = null;
	private static ServerSocket info_serverSocket = null;
	private static ServerSocket lobSocket = null;
	
	private static Socket lobs = null; //lobby socket
	
	private static final int ID_PORT = 3000;
	private static final int PW_PORT = 3001;
	private static final int INFO_PORT = 3002;
	
	// jdbc connection
	private static Connection con;
	
	public static void main(String[] args) {
		try {
			lobSocket = new ServerSocket(4001);
			id_serverSocket = new ServerSocket(ID_PORT); 
			pw_serverSocket = new ServerSocket(PW_PORT);
			info_serverSocket = new ServerSocket(INFO_PORT);
			
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		// ���̵� ��й�ȣ ���� ������
		new Thread(new Runnable() {
			Socket idSocket, pwSocket, infoSocket;
			
			@Override
			public void run() {
				while(true) {
					try {
						idSocket = id_serverSocket.accept();
						pwSocket = pw_serverSocket.accept();
						infoSocket = info_serverSocket.accept();
						
						if(idSocket.getInetAddress().equals(pwSocket.getInetAddress())) {
							System.out.println(" /#"+idSocket.getInetAddress() +" ���� ���α׷� ����");
						}
						
						new Thread(new loginRunnable(idSocket, pwSocket, infoSocket)).start();
						
					}catch(Exception e) {
						e.printStackTrace();
						break;
					}
				}
			}
		}).start();
		
		// Ŭ���̾�Ʈ�� ���� ���̵� ����.
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						lobs = lobSocket.accept();
						
						BufferedReader lob_br = new BufferedReader(new InputStreamReader(lobs.getInputStream()));
						BufferedWriter lob_bw = new BufferedWriter(new OutputStreamWriter(lobs.getOutputStream()));

						String lob_str = lob_br.readLine();
						String lob_ret = findCourse(lob_str);
						lob_bw.write(lob_ret + "\n");
						lob_bw.flush();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
		
		// db����
		try {
			String driverName = "com.mysql.cj.jdbc.Driver";
	        Class.forName(driverName);
	        String DBName = "CourseCommunity";
	        String dbURL = "jdbc:mysql://localhost:3306/" + DBName + "?serverTimezone=UTC";
	        con = DriverManager.getConnection(dbURL, "root", "password");
	        System.out.println("�����ͺ��̽� ���� �Ϸ�");
	      }catch(Exception e) {
	         System.out.println("Mysql ����");
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      }
		
		System.out.println("���� ���� �Ϸ�");
	}// main
	
	// �������� �Ϸķ� ����(%�� ����)
	private static String findCourse(String id) {
		String ret = "";
		try {
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("Select * from takes inner join course on takes.course_id=course.course_id WHERE id='"+ id + "';");
	        while(rs.next()) {
	        	ret+=(String)rs.getObject(3) + "`%" +(String)rs.getObject(4) + "`%" + (String)rs.getObject(5) + "@";
	        }
        }catch(Exception e) { }
		return ret;
	}
	
	// id�� �ش��ϴ� ���� ã��
	private static String[] findInfo(String id) {
		String a[] = {"-1", "-1", "-1", "-1"};
		try {
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("Select * from user inner join login on login.id = user.id WHERE user_id='"+ id + "';");
	        while(rs.next()) {
	        	a[0]=(String)rs.getObject(1) + "@";
	        	a[1]=(String)rs.getObject(2) + "@";
	        	a[2]=(String)rs.getObject(3) + "@";
	        	a[3]=(String)rs.getObject(4) + "@";
	        }
        }catch(Exception e) { }
		return a;
	}
	
	// �α���
	private static boolean login(String id, String pw) {
        try {
        	String pwd_tmp = null;
	        Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("Select * from login WHERE user_id='"+ id + "';");
	        
	        // db cursor_idx�� 0���� �����ϹǷ�
	        while(rs.next()) pwd_tmp=(String)rs.getObject(1);
	        if( pw.equals(pwd_tmp) ) {
	        	rs.close();
	        	stmt.close();
	        	return true;
	        }
	        rs.close();
	    	stmt.close();
        }catch(Exception e) {
        	e.printStackTrace();
        }
		return false;
	}
	
	// �α���SVC
	private static class loginRunnable implements Runnable{
		private BufferedReader id_br, pw_br = null;
		private BufferedWriter info_bw = null;
		private String id_tmp;
		private String pw_tmp;
		
		public loginRunnable(Socket idSocket, Socket pwSocket, Socket infoSocket){
			try {
				id_br = new BufferedReader(new InputStreamReader(idSocket.getInputStream()));
				pw_br = new BufferedReader(new InputStreamReader(pwSocket.getInputStream()));
				info_bw = new BufferedWriter(new OutputStreamWriter(infoSocket.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		@Override
		public void run() {
			try {
				while(true) {
					try {
						id_tmp = id_br.readLine();
						pw_tmp = pw_br.readLine();
						
						// �α��� ����
						if(login(id_tmp, pw_tmp)) {
							String userInfo[] = findInfo(id_tmp);
							info_bw.write(userInfo[0]+userInfo[1]+userInfo[2]+userInfo[3]+"\n");
							info_bw.flush();
							break;
						}
						else {
							// �α��� ����
							info_bw.write("-1@-1@-1@-1\n");
							info_bw.flush();
						}
					}catch(Exception e) {
						id_br.close();
						pw_br.close();
						info_bw.close();
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
