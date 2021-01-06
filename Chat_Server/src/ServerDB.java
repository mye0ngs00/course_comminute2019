import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.mariadb.jdbc.Driver;
/*
 * ���ǻ���-----------------------
 * 1. ���Ƿ� mariaDB defualt�� mysql�ΰ� mariadb�� ��������.
 * 2. port 3306�� mysql�� �浹�ϹǷ� 3307�� ����.
 * 3. ȯ�溯�� ������ �ý��� �浹�ϱ� ������ ������Ʈ ���ٽ� mariadb -u root -p�� ����
 */

public class ServerDB {

	static Connection con;
	static Statement st;
	static ResultSet rs;
	ServerDB(){
		//DataBase
		try {
			String driverName = "org.mariadb.jdbc.Driver";
	        Class.forName(driverName);
	        String jdbcUrl="jdbc:mariadb://localhost:3307/chat?autoReconnect=true";
            String userId="root";
            String userPass="password";
            Connection con = DriverManager.getConnection(jdbcUrl,userId,userPass);
	        st = con.createStatement();
	        
	        System.out.println("�����ͺ��̽� ���� �Ϸ�");
	      }catch(Exception e) {
	         System.out.println("Mysql ����");
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      }
	}
}
