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
	private static final String DRIVER_NAME = "org.mariadb.jdbc.Driver";
	private static final String JDBC_URL = "jdbc:mariadb://localhost:3307/chat?autoReconnect=true";
	private static final String USER_ID = "root";
	private static final String USER_PW = "password";

	static Connection con;
	static Statement st;
	static ResultSet rs;
	
	public ServerDB(){
		try {
	        Class.forName(DRIVER_NAME);
            Connection con = DriverManager.getConnection(JDBC_URL, USER_ID, USER_PW);
	        st = con.createStatement();
	        
	        System.out.println("�����ͺ��̽� ���� �Ϸ�");
	      }catch(Exception e) {
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      }
	}
}
