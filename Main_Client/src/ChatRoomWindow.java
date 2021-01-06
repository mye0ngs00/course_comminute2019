import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChatRoomWindow extends JFrame{
	private JPanel chatField;
	private JTextArea sendMsgField;
	private String course_id;
	private String user_id;
	private String pos;
	private JScrollPane scrollPane;
	private long time;
	private SimpleDateFormat dayTime;
	private String now_time = null;
	
	//img
	private File srcimg;
	private File srcimg3;
	private File srcimg4;
	private Image sendImg;
	private Image bgImg;
	private Image trashImg;
	
	//local-db vary
	private Statement st;
	private ResultSet rs;
	
	ChatRoomWindow(String course_id, String user_id, String pos, Statement st){
		dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		this.course_id = course_id;
		this.user_id = user_id;
		this.pos = pos;
		this.st = st;
		
        setTitle("채팅창");
        // 주의, 여기서 setDefaultCloseOperation() 정의시 X누르면 모든 창이 닫힘
        //이미지
        srcimg = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\sendBtn.png");
        srcimg3 = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\chatbg.png");
        srcimg4 = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\trash.png");
        try {
        	sendImg = ImageIO.read(srcimg).getScaledInstance(62, 62, java.awt.Image.SCALE_SMOOTH);
        	bgImg = ImageIO.read(srcimg3);
        	trashImg = ImageIO.read(srcimg4).getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
        
        JPanel NewWindowContainer = new JPanel();
        setContentPane(NewWindowContainer);
        NewWindowContainer.setLayout(null);
        //teest
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) { 
            	//이 창 X누르면 해시테이블에서 삭제
                SelectChatWindow.courses.remove(course_id);
            }
        });
        //채팅창 이름 label
        JLabel roomName = new JLabel(course_id);
        roomName.setFont(new Font("배달의민족 연성", Font.BOLD, 23));
        roomName.setBounds(17, 14, 94, 21);
        JLabel roomName2 = new JLabel("의 채팅방");
        roomName2.setFont(new Font("배달의민족 연성", Font.PLAIN, 15));
        roomName2.setBounds(111, 16, 85, 21);
        NewWindowContainer.add(roomName);
        NewWindowContainer.add(roomName2);
        
        JLabel trashLabel = new JLabel(new ImageIcon(trashImg));
        trashLabel.setBounds(258, 398, 57, 43);
        trashLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				try {
					st.executeUpdate("delete from chatlog where course_id='"+course_id+"';");
					chatField.removeAll();
					for(int i=0; i<7; i++) chatField.add(new JPanel());
					validate();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
        	
        });
        NewWindowContainer.add(trashLabel);
        
        sendMsgField = new JTextArea();
        sendMsgField.setBounds(17, 444, 238, 85);
        NewWindowContainer.add(sendMsgField);
        sendMsgField.setLineWrap(true); //자동줄바꿈
        sendMsgField.setWrapStyleWord(true); //단어단위로 줄바꿈
        
        sendMsgField.addKeyListener(new KeyAdapter() {
        	//입력받을 글자수 250자로 제한
        	public void keyTyped(KeyEvent ke) {
        		JTextArea src = (JTextArea)ke.getSource();
        		if(src.getText().length()>250) ke.consume();
        		if(src.getText().equals("\n")) src.setText(""); //JtextArea로 진행하기 위함
        	}
        	//엔터키로 전송
        	public void keyPressed(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ENTER){
        			chatSend(sendMsgField.getText());
        			sendMsgField.setText("");
        		}
        	}
        });
        
        //TODO: 말풍선으로 바꾸기
        chatField = new JPanel();
        chatField.setBackground(Color.WHITE);
        chatField.setOpaque(false);
        chatField.setLayout(new GridLayout(0,1,2,2));
        //scroll 최하단에 위치
        scrollPane = new JScrollPane(chatField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(Color.white);
        scrollPane.setOpaque(false);
        scrollPane.setBounds(17, 51, 326, 345);
        NewWindowContainer.add(scrollPane);
        
        //init msg
        try {
			rs = st.executeQuery("select * from chatlog where course_id='"+this.course_id + "';");
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i=0; i<7; i++) chatField.add(new JPanel());
			validate();
			while(rs.next()) {
				//송신인 경우
				if(rs.getString("sender_id").equals(this.user_id)) {
					
					JPanel jp_tmp = new JPanel(new BorderLayout()); //border로 해야하나?
					jp_tmp.setBackground(Color.yellow);
					
					JPanel jp_tmp2 = new JPanel(new GridLayout(2,1,0,0));
					jp_tmp.add(jp_tmp2, BorderLayout.EAST);
					
					
					jp_tmp2.setBackground(Color.yellow);
					jp_tmp2.add(new JLabel(rs.getString("time")+"  " + user_id, JLabel.RIGHT));
					
					//텍스트 생성 ,,
					JTextArea jt_tmp = new JTextArea(rs.getString("payload"));
					jt_tmp.setBackground(Color.yellow);
					jt_tmp.setLineWrap(true); //자동줄바꿈
			        jt_tmp.setWrapStyleWord(true); //단어단위로 줄바꿈
					jt_tmp.setEditable(false);
					//사이즈 20으로 조절
					if(rs.getString("payload").length() > 20) jt_tmp.setColumns(20);
					else jt_tmp.setColumns(rs.getString("payload").length());
					

					jp_tmp2.add(jt_tmp);
			    	chatField.add(jp_tmp);
			    	
				}else {//수신인 경우
					
					JPanel jp_tmp = new JPanel(new GridLayout(2,1,0,0)); //border로 해야하나?
					jp_tmp.setBackground(Color.white);
					if(this.pos.equals("professor")) jp_tmp.add(new JLabel(rs.getString("sender_id")+"  "+rs.getString("time")));
					else jp_tmp.add(new JLabel("익명  "+rs.getString("time")));
					
					//텍스트 생성 ,, 도우미: 부분 아직 추가안함
					JTextArea jt_tmp = new JTextArea(rs.getString("payload"));
					jt_tmp.setBackground(Color.white);
					jt_tmp.setLineWrap(true); //자동줄바꿈
			        jt_tmp.setWrapStyleWord(true); //단어단위로 줄바꿈
					jt_tmp.setEditable(false);
					//사이즈 20으로 조절
					if(rs.getString("payload").length() > 20) jt_tmp.setColumns(20);
					else jt_tmp.setColumns(rs.getString("payload").length());
					
					jp_tmp.add(jt_tmp);
			    	chatField.add(jp_tmp);
				}
				validate();
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
        
        //send msg
        JButton sendMsgBtn = new JButton(new ImageIcon(sendImg));
        sendMsgBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		chatSend(sendMsgField.getText());
        		sendMsgField.setText("");
        	}
        });
        sendMsgBtn.setBounds(258, 444, 85, 85);
        NewWindowContainer.add(sendMsgBtn);
        
        JLabel bgLabel = new JLabel(new ImageIcon(bgImg));
        bgLabel.setBounds(0, 0, 353, 544);
        NewWindowContainer.add(bgLabel);
        
        
        setSize(375,600);
        setVisible(true);
	
        //채팅창 접속시 스크롤 최하단 설정.
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}
	
	//메세지 송신
	public void chatSend(String msg) {
		try {
			time = System.currentTimeMillis(); //현재시간 추출
			now_time = dayTime.format(new Date(time)); // 형식에 맞추기
			SelectChatWindow.bw_msg.write(now_time + "@" + course_id + "@" + user_id + "@" + msg + "\n");
			SelectChatWindow.bw_msg.flush();
			
			JPanel jp_tmp = new JPanel(new BorderLayout()); //border로 해야하나?
			jp_tmp.setBackground(Color.yellow);
			
			JPanel jp_tmp2 = new JPanel(new GridLayout(2,1,0,0));
			jp_tmp.add(jp_tmp2, BorderLayout.EAST);
			
			
			jp_tmp2.setBackground(Color.yellow);
			jp_tmp2.add(new JLabel(now_time+"  " + user_id, JLabel.RIGHT));
			
			//텍스트 생성 ,,
			JTextArea jt_tmp = new JTextArea(msg);
			jt_tmp.setBackground(Color.yellow);
			jt_tmp.setLineWrap(true); //자동줄바꿈
	        jt_tmp.setWrapStyleWord(true); //단어단위로 줄바꿈
			jt_tmp.setEditable(false);
			//사이즈 20으로 조절
			if(msg.length() > 20) jt_tmp.setColumns(20);
			else jt_tmp.setColumns(msg.length());
			

			jp_tmp2.add(jt_tmp);
	    	chatField.add(jp_tmp);
	    	validate(); //컴포넌트 갱신 
	    	
			st.executeUpdate("insert into chatlog values('"+now_time+"','"+user_id+"','"+course_id+"','"+msg+"');");
			//스크롤 하단 설정
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//메세지 수신
    public void chatUpdate(String t, String id, String payload) {
    	now_time = t;
    	//TODO: UI 꾸미기
    	
		
    	JPanel jp_tmp = new JPanel(new GridLayout(2,1,0,0)); //border로 해야하나?
		jp_tmp.setBackground(Color.white);
		if(this.pos.equals("professor")) jp_tmp.add(new JLabel(id+"  "+now_time));
		else jp_tmp.add(new JLabel("익명  "+now_time));
		
		//텍스트 생성 ,, 도우미: 부분 아직 추가안함
		JTextArea jt_tmp = new JTextArea(payload);
		jt_tmp.setBackground(Color.white);
		jt_tmp.setLineWrap(true); //자동줄바꿈
        jt_tmp.setWrapStyleWord(true); //단어단위로 줄바꿈
		jt_tmp.setEditable(false);
		//사이즈 20으로 조절
		if(payload.length() > 20) jt_tmp.setColumns(20);
		else jt_tmp.setColumns(payload.length());
		
		jp_tmp.add(jt_tmp);
    	chatField.add(jp_tmp);
    	validate(); //컴포넌트 갱신
    	//스크롤 하단 고정
    	scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }
}
