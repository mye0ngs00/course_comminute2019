/*  # 여러 개의 소켓을 관리할 수있는 Member 클래스를 따로 만들어 socket 과 입력받을 닉네임을 저장할 Map 객체를 생성하여 관리한다. */
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Member {
	//맵생성
	private Map<Socket, String> mb;
	
	public Member() {
		//해시맵으로 전환
		mb = new HashMap<Socket, String>();
	}
	//get하면 map반환
	public Map<Socket, String> getMb() {
		return mb;
	}
	//해시맵에 소켓이랑 학번 추가
	public void addMember(Socket member, String user_id) {
		this.mb.put(member, user_id);
	}
	//멤버삭제
	public void delMember(Socket p) {
		if (mb.size() == 0) {
			System.out.println("삭제 할 멤버가 없음");
			return;
		}else {
			mb.remove(p);
			System.out.println(p.getInetAddress() + "멤버가 삭제되었습니다.");
		}
	}
}