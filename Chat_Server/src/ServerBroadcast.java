import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

/* ������ ����� Ŭ���̾�Ʈ ��ü���� ��ε�ĳ��Ʈ �ϴ� Ŭ���� */
public class ServerBroadcast implements Runnable {
	private Member member;
	private BufferedWriter bw;
	private Scanner scan;
	private String data;
	private String formatted_data
	
	// ������
	public ServerBroadcast(Member member) {
		this.member = member;
		this.scan = new Scanner(System.in);
	}
	
	// ���Ͽ� �޼��� �Ʊ�
	public void outPutMessage(Map.Entry<Socket, String> s, String msg) {
		try {
			bw = new BufferedWriter(new OutputStreamWriter(s.getKey().getOutputStream(), "UTF-8"));
			bw.write(msg+"\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
	        e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			this.data = scan.nextLine();

			// �������� �ý��� ����.
			if (data.equals("/quit")) {
				System.out.println("quit this system");
				for (Map.Entry<Socket, String> s : this.member.getMb().entrySet())
					outPutMessage(s, "�ý����� �����մϴ�.");
				System.exit(1);
			}
			
			// ���� ��û �޽��� ����
			this.formatted_data = "334A_A@server@" + this.data;
			
			// ��ε�ĳ����
			for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
				outPutMessage(s, this.formatted_data);
			}
		}
	}
}