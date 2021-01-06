import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

public class ServerReceiver implements Runnable {
	private Member member;
	private String data;
	private BufferedReader br;	
	private BufferedWriter bw;
	private boolean stop = true;
	private Socket socket;
	
	private Crawler crawler = null;
	
	//생성자
	public ServerReceiver(Socket socket, Member member) {
		this.socket = socket;
		this.member = member;
		crawler = new Crawler();
		
		//30분에 한 번 크롤링
		new Thread(()->{
			while(true)
				try {
					crawler.Update();
					System.out.println("crawling complete");
					Thread.sleep(1800000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}).start();
	}

	//소켓에 메세지 싣기
	public void outPutMessage(Map.Entry<Socket, String> s, String data) {
		try {
			bw = new BufferedWriter(new OutputStreamWriter(s.getKey().getOutputStream(), "UTF-8"));
			bw.write(data + "\n");
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("outputMessage Exception");
		}
	}

	//소켓에 받은 데이터를 읽고 출력
	public void socketReader() {
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			data = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("socketReader Exception");
			member.delMember(socket);
			if (socket != null)
				socketClose();
			stop = false;
		}
		//데이터 출력
		//System.out.println(data);
	}

	public void socketClose() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("socketClose Exception");
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (stop) {
			// 헷갈리니 그냥 소켓 읽기로 간주하는게 나음.
			socketReader();
			//help에서 송신한 메세지인지??
			try {
				String[] isHelp = this.data.split("#!@");
				//flag가 왔을 경우 맨 처음 flag의 앞이 help인가?
				if(isHelp[0].equals("help")) {
					String commandStr = this.data.substring(7);
					
					try {
						bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
						String temp = ""; //빈 스트링
						//유저가 보내온 메세지 확인
						//퍼포먼스 별로 차이 안나서 그냥 switch-case문 사용. 재미로 넣은 기능
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
							case "야": bw.write("help#!@네??\n"); break;
							case "오": bw.write("help#!@감탄사 5\n"); break;
							case "ㅇ": bw.write("help#!@사람은 아무거나 타이핑 하고 싶을 때 'ㅇ'를 친답니다.\n"); break;
							case "d": bw.write("help#!@사람은 아무거나 타이핑 하고 싶을 때 'd'를 친답니다.\n"); break;
							case "음": bw.write("help#!@고민하지 마세요~\n"); break;
							case "ㅋ": bw.write("help#!@웃기신가요!?\n"); break;
							case "/help": bw.write("help#!@현재 공지 / 행사 / 학사 / 장학 / 취업 만 구현했으며, 이 기능은 동적 해시함수로 수정 예정입니다. 감사합니다.\n"); break;
							default: bw.write("help#!@" + "아직 구현 안됐어요ㅜㅜ /help 를 입력해보세요~\n");
						}
						bw.flush();
						
					}catch(Exception e) {}
					continue;
				}
			}catch(Exception e) {}
			//db저장
			try {
				String[] db_tmp_args = this.data.split("@");
				ServerDB.st.executeUpdate("insert into chatlog(time, sender_id, course_id, payload) "
						+ "values('"+db_tmp_args[0] + "','" + db_tmp_args[2] + "','" + db_tmp_args[1] + "','" + db_tmp_args[3] +"');");
			}catch(Exception e) { System.out.println("DB err");		
			}
			
			//유저학번 찾기
			String user = member.getMb().get(socket); //hash-map 의 key에 대응되는 return value.	
			
			// 퇴장. 
			//TODO: 나중에 강퇴 기능으로 바꿀 것.
			/*
			else if (data.trim().equals("퇴장")) {
				String user = member.getMb().get(socket);
				System.out.println(socket.getInetAddress() + " " + user + " 님이 퇴장하셨습니다.");
				String data = user + "님이 퇴장하셨습니다..";
				for (Map.Entry<Socket, String> s : member.getMb().entrySet()) {
					outPutMessage(s, data);
				}
				member.delMember(socket);
				stop = false;
				socketClose();
			}*/
			
			// 기능
			/*
			else if (data.startsWith("@")) {
				String user = member.getMb().get(socket);
				// 귓속말
				if (data.startsWith("@귓속말/")) {
					String[] whisper = data.split("/");
					String data = "귓속말/" + user + ": " + whisper[2];
					for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
						if (s.getValue().equals(whisper[1])) {
							outPutMessage(s, data);
							break;
						}
					}
				} // 귓속말 종료
			} // 기능 else if 종료. */
			


			
			// 전체 대화
			for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
				if (!s.getKey().equals(socket)) {
					//데이터양식
					String data = new String(this.data);
					outPutMessage(s, data);
				}
			}
		} // while문 종료
	}// run 종료
} 