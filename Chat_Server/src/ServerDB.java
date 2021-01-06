import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.mariadb.jdbc.Driver;
/*
 * 주의사항-----------------------
 * 1. 임의로 mariaDB defualt가 mysql인걸 mariadb로 변경했음.
 * 2. port 3306은 mysql과 충돌하므로 3307로 설정.
 * 3. 환경변수 설정시 시스템 충돌하기 때문에 프롬프트 접근시 mariadb -u root -p로 접속
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
	        
	        System.out.println("데이터베이스 연결 완료");
	      }catch(Exception e) {
	         System.out.println("Mysql 오류");
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      }
	}
}
