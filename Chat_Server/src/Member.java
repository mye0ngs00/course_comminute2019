/*  # ���� ���� ������ ������ ���ִ� Member Ŭ������ ���� ����� socket �� �Է¹��� �г����� ������ Map ��ü�� �����Ͽ� �����Ѵ�. */
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Member {
	//�ʻ���
	private Map<Socket, String> mb;
	
	public Member() {
		//�ؽø����� ��ȯ
		mb = new HashMap<Socket, String>();
	}
	//get�ϸ� map��ȯ
	public Map<Socket, String> getMb() {
		return mb;
	}
	//�ؽøʿ� �����̶� �й� �߰�
	public void addMember(Socket member, String user_id) {
		this.mb.put(member, user_id);
	}
	//�������
	public void delMember(Socket p) {
		if (mb.size() == 0) {
			System.out.println("���� �� ����� ����");
			return;
		}else {
			mb.remove(p);
			System.out.println(p.getInetAddress() + "����� �����Ǿ����ϴ�.");
		}
	}
}