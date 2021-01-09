import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/* 채팅 링크를 관리하는 클래스 */
public class Member {
	// Map(소켓:학번)
	private Map<Socket, String> mb;
	
	public Member() {
		mb = new HashMap<Socket, String>();
	}
	
	// 소켓이랑 학번 추가
	public void addMember(Socket member, String user_id) {
		this.mb.put(member, user_id);
	}
	
	// 멤버 링크 삭제
	public void delMember(Socket p) {
		if (mb.size() == 0) {
			System.out.println("삭제 할 멤버가 없음");
		}else {
			mb.remove(p);
			System.out.println(p.getInetAddress() + "멤버가 삭제되었습니다.");
		}
	}
	
	public Map<Socket, String> getMb() { return mb; }
}