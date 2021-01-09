import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.mariadb.jdbc.Driver;
/*
 * 주의사항-----------------------
 * 1. 임의로 mariaDB defualt가 mysql인걸 mariadb로 변경
 * 2. port 3306은 기존 mysql과 충돌하므로 3307로 설정
 * 3. 환경변수 설정시 시스템 충돌하기 때문에 프롬프트 접근시 mariadb -u root -p로 접속
 */

/* 데이터베이스 설정 */
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
	        
	        System.out.println("데이터베이스 연결 완료");
	      }catch(Exception e) {
	         System.out.println(e.getMessage());
	         e.printStackTrace();
	      }
	}
}
