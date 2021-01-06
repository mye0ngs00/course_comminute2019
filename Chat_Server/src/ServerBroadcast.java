/* # 추가로 서버에서 접속중인 클라이언트 전체에게 같은 내용을 BroadCast 할수 있도록 서버의 Writer를 만든다. */
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

//쓰레드 runnable interface
public class ServerBroadcast implements Runnable {
	private Member member;
	private BufferedWriter bw;
	private Scanner scan;
	private String data;
	//생성자
	public ServerBroadcast(Member member) {
		this.member = member;
		//서버에서 알리는 스캐너
		this.scan = new Scanner(System.in);
	}
	
	//소켓에 메세지 싣기
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

			//서버에서 시스템 종료.
			if (data.equals("/quit")) {
				System.out.println("System Quit");
				for (Map.Entry<Socket, String> s : this.member.getMb().entrySet())
					outPutMessage(s, "시스템을 종료합니다.");
				System.exit(1);
			}
			
			//상대방에게 표출되는 메세지
			String data = "334A_A@server@" + this.data;
			//모든 멤버를 상대로 output
			for (Map.Entry<Socket, String> s : this.member.getMb().entrySet())
				outPutMessage(s, data);
		}
	}
}