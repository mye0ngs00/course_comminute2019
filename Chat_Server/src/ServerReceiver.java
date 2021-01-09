import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

/* 수신 */
public class ServerReceiver implements Runnable {
	private Member member;
	private String data;
	private BufferedReader br;	
	private BufferedWriter bw;
	private boolean flag = true;
	private Socket socket;
	private Crawler crawler = null;
	
	// 생성자
	public ServerReceiver(Socket socket, Member member) {
		this.socket = socket;
		this.member = member;
		this.crawler = new Crawler();
		
		// 30분에 한 번 크롤링
		new Thread(()->{
			while(true) {
				try {
					crawler.Update();
					System.out.println("crawling complete");
					Thread.sleep(1800000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	// 소켓에 메세지 싣기
	public void outPutMessage(Map.Entry<Socket, String> s, String data) {
		try {
			bw = new BufferedWriter(new OutputStreamWriter(s.getKey().getOutputStream(), "UTF-8"));
			bw.write(data + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	// 소켓에 받은 데이터를 읽고 출력
	public void socketReader() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			this.data = br.readLine();
			System.out.println(data);
		} catch (IOException e) {
			// 연결 끊기
			member.delMember(socket);
			if (socket != null) {
				socketClose();
			}
			flag = false;
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	// 소켓 종료
	public void socketClose() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (flag) {
			socketReader();
			try {
				// 데이터를 #!@ 규칙으로 정하고 파싱
				String[] isHelp = this.data.split("#!@");
				if(isHelp[0].equals("help")) {
					String commandStr = this.data.substring(7);
					
					try {
						bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
						String temp = null;
						// 명령어 구분
						switch(commandStr) {
							case "공지":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(0, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(0, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@ㅡㅡㅡ링크를 클릭하시면 이동됩니다ㅡㅡㅡ\n");
								bw.flush();
								break;
							case "행사":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(1, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(1, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@ㅡㅡㅡ링크를 클릭하시면 이동됩니다ㅡㅡㅡ\n");
								bw.flush();
								break;
							case "학사":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(2, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(2, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@ㅡㅡㅡ링크를 클릭하시면 이동됩니다ㅡㅡㅡ\n");
								bw.flush();
								break;
							case "장학":
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(3, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(3, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@ㅡㅡㅡ링크를 클릭하시면 이동됩니다ㅡㅡㅡ\n");
								bw.flush();
								break;
							case "취업":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(4, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(4, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@ㅡㅡㅡ링크를 클릭하시면 이동됩니다ㅡㅡㅡ\n");
								bw.flush();
								break;
							case "야":
								bw.write("help#!@네??\n");
								break;
							case "오":
								bw.write("help#!@감탄사 5\n");
								break;
							case "음":
								bw.write("help#!@고민하지 마세요~\n");
								break;
							case "ㅋ":
								bw.write("help#!@웃기신가요!?\n");
								break;
							case "/help":
								bw.write("help#!@현재 공지 / 행사 / 학사 / 장학 / 취업 만 구현했습니다. 감사합니다.\n");
								break;
							default:
								bw.write("help#!@" + "아직 구현 안됐어요ㅜㅜ /help 를 입력해보세요~\n");
						}
						bw.flush();
					}catch(Exception e) {
						System.out.println(e.getMessage());
						e.printStackTrace();
					}
					// help 기능만 수행하고 넘김.
					continue;
				}
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
			// 수신 메시지 브로드캐스팅
			for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
				if (!s.getKey().equals(socket)) {
					String formatted_data = new String(this.data);
					outPutMessage(s, formatted_data);
				}
			}
						
			// 채팅 기록 저장
			try {
				String[] db_tmp_args = this.data.split("@");
				ServerDB.st.executeUpdate("insert into chatlog(time, sender_id, course_id, payload) "
						+ "values('"+db_tmp_args[0] + "','" + db_tmp_args[2] + "','" + db_tmp_args[1] + "','" + db_tmp_args[3] +"');");
			}catch(Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();		
			}
			
			// 소켓에 연결된 학번 찾기 (귓속말,퇴장 용도)
//			String user = member.getMb().get(socket);	
//			
			// 퇴장
//			
//			else if (data.trim().equals("퇴장")) {
//				String user = member.getMb().get(socket);
//				System.out.println(socket.getInetAddress() + " " + user + " 님이 퇴장하셨습니다.");
//				String data = user + "님이 퇴장하셨습니다..";
//				for (Map.Entry<Socket, String> s : member.getMb().entrySet()) {
//					outPutMessage(s, data);
//				}
//				member.delMember(socket);
//				flag = false;
//				socketClose();
//			}
//			
			// 귓속말
//			else if (data.startsWith("@")) {
//				String user = member.getMb().get(socket);
//				if (data.startsWith("@귓속말/")) {
//					String[] whisper = data.split("/");
//					String data = "귓속말/" + user + ": " + whisper[2];
//					for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
//						if (s.getValue().equals(whisper[1])) {
//							outPutMessage(s, data);
//							break;
//						}
//					}
//				}
//			}
		} // while문 종료
	}// run 종료
} 