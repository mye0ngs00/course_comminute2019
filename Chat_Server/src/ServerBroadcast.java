import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

/* 서버가 연결된 클라이언트 전체에게 브로드캐스트 하는 클래스 */
public class ServerBroadcast implements Runnable {
	private Member member;
	private BufferedWriter bw;
	private Scanner scan;
	private String data;
	private String formatted_data
	
	// 생성자
	public ServerBroadcast(Member member) {
		this.member = member;
		this.scan = new Scanner(System.in);
	}
	
	// 소켓에 메세지 싣기
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

			// 서버에서 시스템 종료.
			if (data.equals("/quit")) {
				System.out.println("quit this system");
				for (Map.Entry<Socket, String> s : this.member.getMb().entrySet())
					outPutMessage(s, "시스템을 종료합니다.");
				System.exit(1);
			}
			
			// 서버 요청 메시지 정제
			this.formatted_data = "334A_A@server@" + this.data;
			
			// 브로드캐스팅
			for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
				outPutMessage(s, this.formatted_data);
			}
		}
	}
}