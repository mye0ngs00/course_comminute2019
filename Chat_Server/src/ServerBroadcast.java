/* # �߰��� �������� �������� Ŭ���̾�Ʈ ��ü���� ���� ������ BroadCast �Ҽ� �ֵ��� ������ Writer�� �����. */
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

//������ runnable interface
public class ServerBroadcast implements Runnable {
	private Member member;
	private BufferedWriter bw;
	private Scanner scan;
	private String data;
	//������
	public ServerBroadcast(Member member) {
		this.member = member;
		//�������� �˸��� ��ĳ��
		this.scan = new Scanner(System.in);
	}
	
	//���Ͽ� �޼��� �Ʊ�
	public void outPutMessage(Map.Entry<Socket, String> s, String data) {
		try {
			bw = new BufferedWriter(new OutputStreamWriter(s.getKey().getOutputStream(), "UTF-8"));
			bw.write(data+"\n");
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Broadcast ouput Exception");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			this.data = scan.nextLine();

			//�������� �ý��� ����.
			if (data.equals("/quit")) {
				System.out.println("System Quit");
				for (Map.Entry<Socket, String> s : this.member.getMb().entrySet())
					outPutMessage(s, "�ý����� �����մϴ�.");
				System.exit(1);
			}
			
			//���濡�� ǥ��Ǵ� �޼���
			String data = "334A_A@server@" + this.data;
			//��� ����� ���� output
			for (Map.Entry<Socket, String> s : this.member.getMb().entrySet())
				outPutMessage(s, data);
		}
	}
}