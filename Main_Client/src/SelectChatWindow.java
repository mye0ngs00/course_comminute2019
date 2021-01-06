import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class SelectChatWindow extends JFrame{
	private static final String ip = "10.50.5.255";
	private static final int port = 4001;
	
	private JScrollPane scrollPane;
	private JButton departBtn;
	//���� ��ư
	private JButton cultureBtn;
	//��ü��ư
	private JButton allBtn;
	//���� ����
	private String userId = null;
	private String userName = null;
	private String deptName = null;
	private String pos = null;
	//���� ����Ʈ
	private JPanel listView;
	//�й� �����ϰ� course_id �ޱ�.
	private Socket hashSocket = null;
	private Socket eeSocket = null;// entry and exit socket�� �׳� ������..��;
	private Socket socket_tmp = null;
	
	private BufferedWriter bw_ee = null;
	private BufferedReader br_msg = null;
	//static�� ������ �ٸ� Ŭ���������� ����ؾ� �ϱ� ����
	static BufferedWriter bw_msg = null;
	//�����ڵ� - ä��â
	static Map<String, ChatRoomWindow> courses=null;
	//�����ڵ� - ä�ø���Ʈ
	private Map<String, myPanel> listInfo = null;
	
	//localdb����. (sqlite, location: eclipse-workspace)
	private static Statement st;
	private static Connection con;
	private static ResultSet rs;
	private Socket recent_msg_soc; //localdb �� serverdb communication
	private BufferedWriter bw_recent_msg_soc;
	private BufferedReader br_recent_msg_soc;
	
	//����� ä�ù�
	private HelpRoomWindow myHelpRoom = null;
	
	//�� â ui img
	private File srcimg= null;
	private File srcimg2= null;
	private File srcimg3= null;
	private File srcimg4 = null;
	private File srcimg5 = null;
	private Image list_img = null;
	private Image chat_img = null;
	private Image qmark_img = null;
	private Image aqmark_img = null;
	private Image check_img = null;
	
	//�� �����г� ����Ʈ
	private myPanel[] my_list = new myPanel[30];
	private static int cnt = 0;
	private ArrayList<Integer> gyIdx = new ArrayList<>(); //������� idxs
	
    SelectChatWindow(String args1, String args2, String args3, String args4) {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.userId = args1;
    	this.userName = args2;
    	this.deptName = args3;
    	this.pos = args4;
    	courses = new HashMap<String, ChatRoomWindow>();
    	listInfo = new HashMap<String, myPanel>();
    	
		
    	if(userId.equals("-1") || userId == null || userName.equals("-1") || userName==null ||deptName.equals("-1") || deptName==null ||
    			pos.equals("-1") || pos==null) {
    		System.out.println("�߸��� �����Դϴ�.");
    		System.exit(0);
    	}
    	
        //courseListView
        listView = new JPanel();
        listView.setBounds(0, 42, 273, 292);
        listView.setLayout(new GridLayout(0,1,0,4));
        
        //ui����
        srcimg = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\chatlistform.png");
        srcimg2 = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\book.png");
        srcimg3 = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\bubble.png");
        srcimg4 = new File("C:\\Users\\myeongsoo\\desktop\\resources\\checkmark2.png");
        srcimg5 = new File("C:\\Users\\myeongsoo\\desktop\\resources\\qmark.png");
        try {
        	list_img = ImageIO.read(srcimg);
        	qmark_img = ImageIO.read(srcimg2).getScaledInstance(92, 62, java.awt.Image.SCALE_SMOOTH);
        	check_img = ImageIO.read(srcimg4).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        	chat_img = ImageIO.read(srcimg3).getScaledInstance(46, 46, java.awt.Image.SCALE_SMOOTH);
        	aqmark_img = ImageIO.read(srcimg5).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        } catch (IOException e2) {
        	e2.printStackTrace();
        }
        
    	//�й� �����ϰ� course_id �ޱ�
    	try {
			hashSocket = new Socket(ip,port);
			BufferedReader hs_br = new BufferedReader(new InputStreamReader(hashSocket.getInputStream(), "UTF-8"));
			BufferedWriter hs_bw = new BufferedWriter(new OutputStreamWriter(hashSocket.getOutputStream(), "UTF-8"));
			
			hs_bw.write(userId + "\n");
			hs_bw.flush();
			String hs_str = hs_br.readLine();
			if(hs_str.equals("")) {
				//TODO: ���������� ����
				System.exit(0);
			}else {
				String hs_tmp[] = hs_str.split("@");
				
				socket_tmp = new Socket(ip, 4000);
				eeSocket = new Socket(ip, 4002);
				
				bw_ee = new BufferedWriter(new OutputStreamWriter(eeSocket.getOutputStream(), "UTF-8"));
				br_msg = new BufferedReader(new InputStreamReader(socket_tmp.getInputStream(), "UTF-8"));
				bw_msg = new BufferedWriter(new OutputStreamWriter(socket_tmp.getOutputStream(), "UTF-8"));
				
				//4002�� ��Ʈ�� �� �й��� ������.
				bw_ee.write(userId + "\n");
				bw_ee.flush();
				//���� �߰�
				for(String course : hs_tmp) {
					if(course.equals("")) break;//TODO: ���̻� ���� ����
					
					String c_info[] = course.split("`%");
					my_list[cnt++] = new myPanel(c_info[0],c_info[1], c_info[2]);
					listInfo.put(c_info[0], my_list[cnt-1]);
					listView.add(my_list[cnt-1].getJPanel());
					if(c_info[1].equals("GY")) gyIdx.add(cnt-1);
					validate();
					//new Thread(new ChatSocketReader(socket_tmp)).start();
				}
				//�� ĭ ä���
				if(cnt < 7) {
					int cnt_tmp = cnt;
					while(cnt_tmp < 7) {
						listView.add(new JPanel());
						cnt_tmp++;
					}
					validate();
				}
				//�а� �Ѹ���
				new Thread(new ChatBuffer()).start();
				
				
			}
			
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        setTitle("���Ǹ��");
        // ����, ���⼭ setDefaultCloseOperation() ���ǽ� X������ ��� â�� ����
        JPanel NewWindowContainer = new JPanel();
        setContentPane(NewWindowContainer);
        NewWindowContainer.setLayout(null);
        
        setSize(465,647);
        setResizable(false);
        setVisible(true);
        
		//ScrollPane�� listview �߰� & ���� ��ũ�� & ���� ��ũ�� �����
		scrollPane = new JScrollPane(listView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(21, 137, 430, 388);
		NewWindowContainer.add(scrollPane);
		
		//default ��ü��ư üũ (üũ ��ư)
		JLabel allCheck = new JLabel(new ImageIcon(check_img));
		allCheck.setBounds(311, 27, 140, 60);
		NewWindowContainer.add(allCheck);
		
		JLabel deptCheck = new JLabel(new ImageIcon(check_img));
		deptCheck.setBounds(12, 27, 140, 60);
		deptCheck.setVisible(false);
		NewWindowContainer.add(deptCheck);
		
		JLabel cultureCheck = new JLabel(new ImageIcon(check_img));
		cultureCheck.setBounds(154, 27, 140, 60);
		cultureCheck.setVisible(false);
		NewWindowContainer.add(cultureCheck);
		
		
		//ä��â ���
		JLabel windowName = new JLabel("�����ϴ� ���� ���", JLabel.CENTER);
		windowName.setFont(new Font("���ѹα����λ�¡ü R",Font.PLAIN, 20)); //���� �Ӽ�����
		windowName.setBounds(17, 0, 412, 53);
		NewWindowContainer.add(windowName);
		
		JLabel justLabel = new JLabel("ä�á�", JLabel.LEFT);
		justLabel.setFont(new Font("���ѹα����λ�¡ü R",Font.PLAIN, 14)); //���� �Ӽ�����
		justLabel.setBounds(21, 114, 54, 25);
		NewWindowContainer.add(justLabel);
		
		
		//��ü��ư
		allBtn = new JButton();
		allBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cultureCheck.setVisible(false);
				allCheck.setVisible(true);
				deptCheck.setVisible(false);
				
				listView.removeAll();
				for(int i=0; i<cnt; i++) listView.add(my_list[i].getJPanel());
				
				if(cnt < 7) {
					int cnt_tmp = cnt;
					while(cnt_tmp < 7) {
						listView.add(new JPanel());
						cnt_tmp++;
					}
				}
				validate();
			}
		});
		allBtn.setBackground(Color.white);
		allBtn.setOpaque(false);
		allBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		allBtn.setFocusPainted(true);;
		
		JLabel lblNewLabel_1 = new JLabel(userName +"�� ȯ���մϴ�!");
		lblNewLabel_1.setFont(new Font("���ѹα����λ�¡ü R", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(21, 532, 273, 21);
		NewWindowContainer.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("����: "+pos + "  , �й�: " + userId);
		lblNewLabel_2.setFont(new Font("���ѹα����λ�¡ü R", Font.PLAIN, 15));
		lblNewLabel_2.setBounds(21, 556, 273, 21);
		NewWindowContainer.add(lblNewLabel_2);
		allBtn.setBounds(311, 54, 140, 60);
		NewWindowContainer.add(allBtn);
		
		//�������� ��ư
		departBtn = new JButton();
		departBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cultureCheck.setVisible(false);
				allCheck.setVisible(false);
				deptCheck.setVisible(true);
				
				listView.removeAll();
				int i=0;
				for(i=0; i<cnt; i++) listView.add(my_list[i].getJPanel());
				for(int idx : gyIdx) {
					listView.remove(my_list[idx].getJPanel());
					i--;
				}
				for(;i <7;i++) listView.add(new JPanel()); //�ؿ��� __cnt__���� ���.
				validate();
			}
		});
		departBtn.setBackground(Color.white);
		departBtn.setOpaque(false);
		departBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		departBtn.setFocusPainted(true);
		departBtn.setBounds(11, 54, 141, 60);
		NewWindowContainer.add(departBtn);
		
		//������� ��ư
		cultureBtn = new JButton();
		cultureBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cultureCheck.setVisible(true);
				allCheck.setVisible(false);
				deptCheck.setVisible(false);
			
				listView.removeAll();
				int __cnt__ = 0;
				for(int idx : gyIdx) {
					listView.add(my_list[idx].getJPanel());
					__cnt__++;
				}
				for(;__cnt__ <7;__cnt__++) listView.add(new JPanel());
				validate();
			}
		});
		cultureBtn.setBackground(Color.white);
		cultureBtn.setOpaque(false);
		cultureBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		cultureBtn.setFocusPainted(true);
		cultureBtn.setBounds(158, 54, 140, 60);
		NewWindowContainer.add(cultureBtn);
		
		//����� ��ư
		JButton noticeBtn = new JButton(new ImageIcon(qmark_img));
		noticeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myHelpRoom = new HelpRoomWindow(userId);
			}
		}); //hover�� ����ǥ
		JLabel noticeLb = new JLabel(new ImageIcon(aqmark_img));
		noticeBtn.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {		}
			public void mouseEntered(MouseEvent e) {noticeLb.setVisible(true);}
			public void mouseExited(MouseEvent e) {noticeLb.setVisible(false);}
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		noticeLb.setBounds(329, 499, 102, 107);
		noticeLb.setVisible(false);
		NewWindowContainer.add(noticeLb);
		noticeBtn.setBackground(Color.white);
		noticeBtn.setOpaque(false);
		noticeBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		noticeBtn.setFocusPainted(true);
		noticeBtn.setBounds(316, 532, 135, 75);
		NewWindowContainer.add(noticeBtn);
		JLabel lblNewLabel = new JLabel(new ImageIcon(list_img));
		lblNewLabel.setBounds(0, 0, 459, 607);
		NewWindowContainer.add(lblNewLabel);
		
		//db����
		try {
			
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:chat.db");
			st = con.createStatement();
			
			//���� ���϶ձ�
			recent_msg_soc = new Socket(ip,4006);
			br_recent_msg_soc = new BufferedReader(new InputStreamReader(recent_msg_soc.getInputStream()));
			bw_recent_msg_soc = new BufferedWriter(new OutputStreamWriter(recent_msg_soc.getOutputStream()));
			
			try {
			//���̺� ����. ����� �ѱ�
			st.executeUpdate("create table chatlog ("
	        		+ "time varchar(20),"
	        		+ "sender_id varchar(60),"
	        		+ "course_id varchar(60),"
	        		+ "payload varchar(255));");
			}catch(Exception e) {}
			
			//�ʿ�� ���� ��ɾ�
			//st.executeUpdate("delete from chatlog;");
			rs = st.executeQuery("SELECT * FROM chatlog ORDER BY time DESC limit 1;");
			//System.out.println(rs.getString("time"));
			while(rs.next()) {
				bw_recent_msg_soc.write(rs.getString("time")+"@"+rs.getString("sender_id")+"@" +rs.getString("course_id")+"@"
						+ rs.getString("payload"));
				bw_recent_msg_soc.flush();
			}
			//���� �����鼭 localDB ä���
			new Thread(()->{
				String tmp_msgLog = null;
				while(true) {
					try {
						tmp_msgLog = br_recent_msg_soc.readLine();
						if(tmp_msgLog.equals("-1")) {
							System.out.println("localDB ���� �Ϸ�");
							break;
						}
						String[] tmp2_msgLog = tmp_msgLog.split("@");
						st.executeUpdate("insert into chatlog values('"+ tmp2_msgLog[0]+"','"
								+ tmp2_msgLog[1]+"','"+ tmp2_msgLog[2]+"','"+ tmp2_msgLog[3]+"');");
						//���� �� ���̵�� ���� ����̶� ���� ������
						if(!(tmp2_msgLog[1].equals(userId))) listInfo.get(tmp2_msgLog[2]).turnMsg(true);
					} catch (Exception e1) {
						break;
					}
				}
			}).start();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }
    
    private class myPanel{
    	private JPanel courseTmp = null;
    	private String dept_name = null;
    	private String course_id = null;
    	private String title = null;
    	private JLabel courseLabel1 = null;
    	private JTextArea courseLabel2 = null;
    	private JLabel  courseLabel3 = null;
    	
    	myPanel(){	}
    	myPanel(String course_id, String dept_name, String title){
    		this.course_id = course_id;
    		this.dept_name = dept_name;
    		this.title = title;
    		
    		//course_id + ", " + dept_name + ", " + title
        	courseTmp = new JPanel(new GridLayout(1,4,0,1));
        	courseTmp.setBackground(Color.white);
        	courseLabel1 = new JLabel(new ImageIcon(chat_img));
        	if(dept_name.equals("GY")) dept_name = "GY(����)";
        	courseLabel2 = new JTextArea("���Ǹ�: "+title + "\n�����ڵ�: "+course_id + "\n���� �а�: "+dept_name);
        	courseLabel2.setFont(new Font("����ǹ��� ����",Font.PLAIN, 13));
        	courseLabel2.setEditable(false);
        	courseLabel2.setFocusable(false);
        	courseLabel2.setBackground(Color.white);
        	courseLabel2.setOpaque(false);
        	//new message
        	courseLabel3 = new JLabel("�� �޽���");
        	courseLabel3.setVisible(false);
        	// JLabel ���󺯰��ϱ� ���ؼ��� setOpaque�� true�� ������Ѵ�.
        	courseLabel3.setForeground(Color.magenta);
        	courseLabel3.setFont(new Font("���ѹα����λ�¡ü R",Font.PLAIN, 13));
        	courseTmp.add(courseLabel1);
        	courseTmp.add(courseLabel2);
        	courseTmp.add(courseLabel3);
        	//���� Ŭ���� �ߵ�
        	courseTmp.addMouseListener(new MouseListener() {
    			@Override
    			public void mouseClicked(MouseEvent arg0) {	}
    			@Override
    			public void mouseEntered(MouseEvent arg0) {
    				courseTmp.setBackground(Color.lightGray);
    				courseLabel2.setBackground(Color.lightGray);
    			}
    			@Override
    			public void mouseExited(MouseEvent arg0) {
    				courseTmp.setBackground(Color.white);
    				courseLabel2.setBackground(Color.white);
    			}
    			@Override
    			public void mousePressed(MouseEvent arg0) {
    					//�ؽ����̺� �߰�
    					if (arg0.getClickCount() == 2) {
    						courses.put(course_id, new ChatRoomWindow(course_id, userId, pos, st));
    						listInfo.get(course_id).turnMsg(false);
    					}
    				}
    			@Override
    			public void mouseReleased(MouseEvent e) {}
    			
        	});
        	courseLabel2.addMouseListener(new MouseListener() {
    			@Override
    			public void mouseClicked(MouseEvent arg0) {	}
    			@Override
    			public void mouseEntered(MouseEvent arg0) {
    				courseTmp.setBackground(Color.lightGray);
    				courseLabel2.setBackground(Color.lightGray);
    			}
    			@Override
    			public void mouseExited(MouseEvent arg0) {
    				courseTmp.setBackground(Color.white);
    				courseLabel2.setBackground(Color.white);
    			}
    			@Override
    			public void mousePressed(MouseEvent arg0) {
    					//�ؽ����̺� �߰�
    					if (arg0.getClickCount() == 2) {
    						courses.put(course_id, new ChatRoomWindow(course_id, userId, pos, st));
    						listInfo.get(course_id).turnMsg(false);
    					}
    				}
    			@Override
    			public void mouseReleased(MouseEvent e) {}
    			
        	});

    	}
    	public void turnMsg(boolean b) {
    		this.courseLabel3.setVisible(b);
    	}
    	public JPanel getJPanel() {
    		return this.courseTmp;
    	}
    }
    
    private class ChatBuffer implements Runnable{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true) {
				try {
					String msg = br_msg.readLine();
					if(msg.split("#!@")[0].equals("help")) {
						myHelpRoom.chatUpdate(msg.substring(7));
						continue;
					}
					String[] msg2 = msg.split("@");
					msg = msg.substring(msg2[0].length() + msg2[1].length() + msg2[2].length() + 3);
					//�ش� course_id�� ä��â�� id�� msg ����
					//TODO: �ش� ä��â�� ������ �ִ��� Ȯ��
					try {
						courses.get(msg2[1]).chatUpdate(msg2[0], msg2[2], msg);
					}catch(Exception e) {listInfo.get(msg2[1]).turnMsg(true);} //������ ���
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					break;
				}
			}
		}
    	
    }
}
