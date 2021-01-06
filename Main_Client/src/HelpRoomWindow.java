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
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpRoomWindow extends JFrame{
	private String userId = null;
	private static JPanel chatField = null;
	private static JScrollPane scrollPane = null;
	private WebURI webUri = new WebURI();
	
	//img
	private File srcimg;
	private File srcimg3;
	private File srcimg4;
	private Image sendImg;
	private Image bgImg;
	private Image trashImg;
	
	HelpRoomWindow(String userId){
		this.userId = userId;
		
		setTitle("ä��â");
        // ����, ���⼭ setDefaultCloseOperation() ���ǽ� X������ ��� â�� ����
        
        JPanel NewWindowContainer = new JPanel();
        setContentPane(NewWindowContainer);
        NewWindowContainer.setLayout(null);
        
        //ä��â �̸� label
        JLabel roomName = new JLabel("�����");
        roomName.setFont(new Font("����ǹ��� ����", Font.BOLD, 23));
        roomName.setBounds(17, 14, 67, 21);
        JLabel roomName2 = new JLabel("�� ä�ù�");
        roomName2.setFont(new Font("����ǹ��� ����", Font.PLAIN, 15));
        roomName2.setBounds(91, 16, 85, 21);
        NewWindowContainer.add(roomName);
        NewWindowContainer.add(roomName2);
        //�̹���
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
        
        //TODO: ��ǳ������ �ٲٱ�
        chatField = new JPanel();
        chatField.setBackground(Color.WHITE);
        chatField.setLayout(new GridLayout(0,1,2,2));
        //scroll ���ϴܿ� ��ġ
        for(int i=0; i<7; i++) chatField.add(new JPanel());
        scrollPane = new JScrollPane(chatField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(17, 51, 326, 345);
        NewWindowContainer.add(scrollPane);
        
        JTextArea sendMsgField = new JTextArea();
        sendMsgField.setLineWrap(true); //�ڵ��ٹٲ�
        sendMsgField.setWrapStyleWord(true); //�ܾ������ �ٹٲ�
        
        sendMsgField.setBounds(17, 444, 237, 85);
        NewWindowContainer.add(sendMsgField);

        sendMsgField.addKeyListener(new KeyAdapter() {
        	//�Է¹��� ���ڼ� 250�ڷ� ����
        	public void keyTyped(KeyEvent ke) {
        		JTextArea src = (JTextArea)ke.getSource();
        		if(src.getText().length()>250) ke.consume();
        		if(src.getText().equals("\n")) src.setText(""); //JtextArea�� �����ϱ� ����
        	}
        	//����Ű�� ����
        	public void keyPressed(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ENTER){
        			chatSend(sendMsgField.getText());
        			sendMsgField.setText("");
        		}
        	}
        });
        
        //send msg
        JButton sendMsgBtn = new JButton(new ImageIcon(sendImg));
        sendMsgBtn.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		chatSend(sendMsgField.getText());
        		try {
					Thread.sleep(400);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		sendMsgField.setText("");
        	}
        });
        sendMsgBtn.setBounds(258, 444, 85, 85);
        NewWindowContainer.add(sendMsgBtn);
        
        JLabel trashLabel = new JLabel(new ImageIcon(trashImg));
        trashLabel.setBounds(258, 398, 57, 43);
        trashLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) { 
				chatField.removeAll();
				for(int i=0; i<7; i++) chatField.add(new JPanel());
				validate();
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
        
        JLabel bgLabel = new JLabel(new ImageIcon(bgImg));
        bgLabel.setBounds(0, 0, 353, 544);
        NewWindowContainer.add(bgLabel);
        
        setSize(375,600);
        setVisible(true);
        
        //ä��â ���ӽ� ��ũ�� ���ϴ� ����.
        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        
        
	}
	
	//����� �޼��� �۽�
	public void chatSend(String msg) {
		try {
			SelectChatWindow.bw_msg.write("help#!@"+msg+"\n");
			SelectChatWindow.bw_msg.flush();
			
			JPanel jp_tmp = new JPanel(new BorderLayout()); //border�� �ؾ��ϳ�?
			jp_tmp.setBackground(Color.orange);
			
			JPanel jp_tmp2 = new JPanel(new GridLayout(2,1,0,0));
			jp_tmp.add(jp_tmp2, BorderLayout.EAST);
			
			
			jp_tmp2.setBackground(Color.orange);
			jp_tmp2.add(new JLabel(userId, JLabel.RIGHT));
			
			//�ؽ�Ʈ ���� ,,
			JTextArea jt_tmp = new JTextArea(msg);
			jt_tmp.setBackground(Color.orange);
			jt_tmp.setLineWrap(true); //�ڵ��ٹٲ�
	        jt_tmp.setWrapStyleWord(true); //�ܾ������ �ٹٲ�
			jt_tmp.setEditable(false);
			//������ 20���� ����
			if(msg.length() > 20) jt_tmp.setColumns(20);
			else jt_tmp.setColumns(msg.length());
			

			jp_tmp2.add(jt_tmp);
	    	chatField.add(jp_tmp);
	    	validate(); //������Ʈ ���� 

			//��ũ�� �ϴ� ����
			scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//����� �޼��� ����
    public void chatUpdate(String payload) {
    	//TODO: UI �ٹ̱�
		JPanel jp_tmp = new JPanel(new GridLayout(2,1,0,0)); //border�� �ؾ��ϳ�?
		jp_tmp.setBackground(Color.yellow);
		jp_tmp.add(new JLabel("�����"));
		
		//ȭ�� �߾� ��ġ
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������
	
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
		//�ؽ�Ʈ ���� ,, �����: �κ� ���� �߰�����
		JTextArea jt_tmp = new JTextArea(payload);
		jt_tmp.setRows(1); //test
		jt_tmp.setBackground(Color.yellow);
		jt_tmp.setLineWrap(true); //�ڵ��ٹٲ�
        jt_tmp.setWrapStyleWord(true); //�ܾ������ �ٹٲ�
		jt_tmp.setEditable(false);
		//������ 20���� ����
		try {
			//url Ŭ���� ��ǥ�� �̵�.
			if(payload.substring(0,4).equals("http"))
				jt_tmp.addMouseListener(new MouseListener() {
					@Override
					public void mouseClicked(MouseEvent e) {webUri.enterWeb(payload);}

					@Override
					public void mouseEntered(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseReleased(MouseEvent e) {}
					
				});
		}catch(Exception e) {}
		if(payload.length() > 20) jt_tmp.setColumns(20);
		else jt_tmp.setColumns(payload.length());
		
		jp_tmp.add(jt_tmp);
    	chatField.add(jp_tmp);
    	validate(); //������Ʈ ����
    	//��ũ�� �ϴ� ����
    	scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }
}
