import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.mariadb.jdbc.Driver;
/*
 * ���ǻ���-----------------------
 * 1. ���Ƿ� mariaDB defualt�� mysql�ΰ� mariadb�� ����
 * 2. port 3306�� ���� mysql�� �浹�ϹǷ� 3307�� ����
 * 3. ȯ�溯�� ������ �ý��� �浹�ϱ� ������ ������Ʈ ���ٽ� mariadb -u root -p�� ����
 */

/* �����ͺ��̽� ���� */
public class ServerDB {
	private static final String driverName = "org.mariadb.jdbc.Driver";
	private static final String jdbcUrl="jdbc:mariadb://localhost:3307/chat?autoReconnect=true";
	static Connection con;
	static Statement st;
	static ResultSet rs;
	
	public ServerDB(){
		try {
	        Class.forName(driverName);
            String userId="root";
            String userPw="password";
            Connection con = DriverManager.getConnection(jdbcUrl,userId,userPw);
	        st = con.createStatement();
	        
	        System.out.println("�����ͺ��̽� ���� �Ϸ�");
	      }catch(Exception e) {
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      }
	}
}
