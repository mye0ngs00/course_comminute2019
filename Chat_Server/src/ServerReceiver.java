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
	
	//������
	public ServerReceiver(Socket socket, Member member) {
		this.socket = socket;
		this.member = member;
		crawler = new Crawler();
		
		//30�п� �� �� ũ�Ѹ�
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

	//���Ͽ� �޼��� �Ʊ�
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

	//���Ͽ� ���� �����͸� �а� ���
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
		//������ ���
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
			// �򰥸��� �׳� ���� �б�� �����ϴ°� ����.
			socketReader();
			//help���� �۽��� �޼�������??
			try {
				String[] isHelp = this.data.split("#!@");
				//flag�� ���� ��� �� ó�� flag�� ���� help�ΰ�?
				if(isHelp[0].equals("help")) {
					String commandStr = this.data.substring(7);
					
					try {
						bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
						String temp = ""; //�� ��Ʈ��
						//������ ������ �޼��� Ȯ��
						//�����ս� ���� ���� �ȳ��� �׳� switch-case�� ���. ��̷� ���� ���
						switch(commandStr) {
							case "����":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(0, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(0, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@�ѤѤѸ�ũ�� Ŭ���Ͻø� �̵��˴ϴ٤ѤѤ�\n");
								bw.flush();
								break;
							case "���":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(1, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(1, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@�ѤѤѸ�ũ�� Ŭ���Ͻø� �̵��˴ϴ٤ѤѤ�\n");
								bw.flush();
								break;
							case "�л�":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(2, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(2, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@�ѤѤѸ�ũ�� Ŭ���Ͻø� �̵��˴ϴ٤ѤѤ�\n");
								bw.flush();
								break;
							case "����":
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(3, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(3, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@�ѤѤѸ�ũ�� Ŭ���Ͻø� �̵��˴ϴ٤ѤѤ�\n");
								bw.flush();
								break;
							case "���":	
								for(int i=0; i< 7; i++) {
									bw.write("help#!@" + crawler.getNoticeText(4, i)+"\n");
									bw.flush();
									bw.write("help#!@" + crawler.getNoticeLink(4, i)+"\n");
									bw.flush();
								}
								bw.write("help#!@�ѤѤѸ�ũ�� Ŭ���Ͻø� �̵��˴ϴ٤ѤѤ�\n");
								bw.flush();
								break;
							case "��": bw.write("help#!@��??\n"); break;
							case "��": bw.write("help#!@��ź�� 5\n"); break;
							case "��": bw.write("help#!@����� �ƹ��ų� Ÿ���� �ϰ� ���� �� '��'�� ģ��ϴ�.\n"); break;
							case "d": bw.write("help#!@����� �ƹ��ų� Ÿ���� �ϰ� ���� �� 'd'�� ģ��ϴ�.\n"); break;
							case "��": bw.write("help#!@������� ������~\n"); break;
							case "��": bw.write("help#!@����Ű���!?\n"); break;
							case "/help": bw.write("help#!@���� ���� / ��� / �л� / ���� / ��� �� ����������, �� ����� ���� �ؽ��Լ��� ���� �����Դϴ�. �����մϴ�.\n"); break;
							default: bw.write("help#!@" + "���� ���� �ȵƾ��̤� /help �� �Է��غ�����~\n");
						}
						bw.flush();
						
					}catch(Exception e) {}
					continue;
				}
			}catch(Exception e) {}
			//db����
			try {
				String[] db_tmp_args = this.data.split("@");
				ServerDB.st.executeUpdate("insert into chatlog(time, sender_id, course_id, payload) "
						+ "values('"+db_tmp_args[0] + "','" + db_tmp_args[2] + "','" + db_tmp_args[1] + "','" + db_tmp_args[3] +"');");
			}catch(Exception e) { System.out.println("DB err");		
			}
			
			//�����й� ã��
			String user = member.getMb().get(socket); //hash-map �� key�� �����Ǵ� return value.	
			
			// ����. 
			//TODO: ���߿� ���� ������� �ٲ� ��.
			/*
			else if (data.trim().equals("����")) {
				String user = member.getMb().get(socket);
				System.out.println(socket.getInetAddress() + " " + user + " ���� �����ϼ̽��ϴ�.");
				String data = user + "���� �����ϼ̽��ϴ�..";
				for (Map.Entry<Socket, String> s : member.getMb().entrySet()) {
					outPutMessage(s, data);
				}
				member.delMember(socket);
				stop = false;
				socketClose();
			}*/
			
			// ���
			/*
			else if (data.startsWith("@")) {
				String user = member.getMb().get(socket);
				// �ӼӸ�
				if (data.startsWith("@�ӼӸ�/")) {
					String[] whisper = data.split("/");
					String data = "�ӼӸ�/" + user + ": " + whisper[2];
					for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
						if (s.getValue().equals(whisper[1])) {
							outPutMessage(s, data);
							break;
						}
					}
				} // �ӼӸ� ����
			} // ��� else if ����. */
			


			
			// ��ü ��ȭ
			for (Map.Entry<Socket, String> s : this.member.getMb().entrySet()) {
				if (!s.getKey().equals(socket)) {
					//�����;��
					String data = new String(this.data);
					outPutMessage(s, data);
				}
			}
		} // while�� ����
	}// run ����
} 