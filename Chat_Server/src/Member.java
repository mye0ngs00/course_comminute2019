import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/* ä�� ��ũ�� �����ϴ� Ŭ���� */
public class Member {
	// Map(����:�й�)
	private Map<Socket, String> mb;
	
	public Member() {
		mb = new HashMap<Socket, String>();
	}
	
	// �����̶� �й� �߰�
	public void addMember(Socket member, String user_id) {
		this.mb.put(member, user_id);
	}
	
	// ��� ��ũ ����
	public void delMember(Socket p) {
		if (mb.size() == 0) {
			System.out.println("���� �� ����� ����");
		}else {
			mb.remove(p);
			System.out.println(p.getInetAddress() + "����� �����Ǿ����ϴ�.");
		}
	}
	
	public Map<Socket, String> getMb() { return mb; }
}