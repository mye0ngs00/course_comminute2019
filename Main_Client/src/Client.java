import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class Client extends JFrame {
	
	private static String ipNumber = "10.50.5.255"; // 연결할 IP 
	private static int portNumber = 3000; // 연결할 Port 번호
	
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField textField;
	private JPanel inputPanel;
	private JButton loginBtn;
	private JButton forgotBtn;
	private Image logo_img = null;
	private Image login_img = null;
	//로그인 패널
	private JPanel loginPanel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel2;
	//로고이미지 소스코드
	private File srcimg;
	private File srcimg2;
	
	//id,pw 소켓
	private static Socket idSocket = null;
	private static Socket pwSocket = null;
	private static volatile BufferedWriter id_bw;
	private static volatile BufferedWriter pw_bw;
	
	//유저
	private String userId = null;
	private String userName = null;
	private String deptName = null;
	private String pos = null;
	
	//user info socket
	private static Socket userInfoSocket = null;
	private static String receivedInfo = null;
	private static volatile BufferedReader info_br;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			/*
			if(args.length != 2) {
				System.out.println("Server IP, Port 입력 바랍니다.");
				System.out.println("잘못된 실행이여서 프로그램을 종료합니다.");
				System.exit(0);
			}
			else{
				ipNumber = args[0];
				portNumber = Integer.parseInt(args[1]);
			}*/
			idSocket = new Socket(ipNumber, portNumber);
			pwSocket = new Socket(ipNumber, portNumber+1);
			userInfoSocket = new Socket(ipNumber, portNumber+2);
			info_br = new BufferedReader(new InputStreamReader(userInfoSocket.getInputStream(), "UTF-8"));
			id_bw = new BufferedWriter(new OutputStreamWriter(idSocket.getOutputStream(), "UTF-8"));
			pw_bw = new BufferedWriter(new OutputStreamWriter(pwSocket.getOutputStream(), "UTF-8"));
			
		}catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
			
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
					frame.setTitle("CourseCommunity : SignIn");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	/**
	 * Create the frame.
	 */
	public Client() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 596, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//화면 중앙 배치
		Dimension frameSize = this.getSize(); // 프레임 사이즈
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈

		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
		//this.setUndecorated(true); //창 프레임 없애기. visible전에 해야함.
		this.setResizable(false); //창 크기 변경 불가능
		
		//맨 처음 패널
		loginPanel = new JPanel();
		loginPanel.setBounds(0, 0, 601, 565);
		contentPane.add(loginPanel);
		loginPanel.setLayout(null);
		
		//로그인 버튼 이벤트
		loginBtn = new JButton("");
		//버튼 투명하게
		loginBtn.setBackground(Color.white);
		loginBtn.setOpaque(false);
		loginBtn.setFocusPainted(true);
		loginBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //테두리 없애기
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//로그인
				login();
			}
		});
		
		
		loginBtn.setBounds(87, 346, 421, 62);
		loginPanel.add(loginBtn);
		
		//비밀번호 찾기 버튼 이벤트
		forgotBtn = new JButton("");
		forgotBtn.setLocation(195, 464);
		forgotBtn.setSize(216, 44);
		forgotBtn.setBackground(Color.white);
		forgotBtn.setOpaque(false);
		forgotBtn.setFocusPainted(true);
		forgotBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //테두리 없애기
		forgotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//비밀번호 찾기
				new FindPassword();
			}
		});
		loginPanel.add(forgotBtn);
		
		//이미지 불러오기
		srcimg = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\logo.png"); //상단배너
		srcimg2 = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\loginform.png"); //로그인 폼
		try {
			logo_img = ImageIO.read(srcimg).getScaledInstance(330, 70, java.awt.Image.SCALE_SMOOTH);
			login_img = ImageIO.read(srcimg2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//상단배너
		lblNewLabel = new JLabel(new ImageIcon(logo_img));
		lblNewLabel.setBounds(132, 76, 330, 70);
		loginPanel.add(lblNewLabel);
		
		lblNewLabel2 = new JLabel(new ImageIcon(login_img));
		lblNewLabel2.setBounds(0, 0, 601, 567);
		loginPanel.add(lblNewLabel2);
		
		
		//아이디 입력
		textField = new JTextField();
		textField.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //테두리 없애기
		textField.setBounds(132, 185, 369, 53);
		loginPanel.add(textField);
		textField.setColumns(10);
		
		//비밀번호 입력
		passwordField = new JPasswordField();
		passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //테두리 없애기
		passwordField.setBounds(132, 267, 369, 53);
		loginPanel.add(passwordField);
		passwordField.addKeyListener(new loginWithEnterAdapter());
		
		//엔터로 로그인
		textField.addKeyListener(new loginWithEnterAdapter());
		
	}
	//로그인 시도. 소켓으로 id/pw 전송
	private void login() {
		try {
			id_bw.write(textField.getText()+"\n");
			pw_bw.write(String.valueOf(passwordField.getPassword())+"\n");
			id_bw.flush();
			pw_bw.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
			try {
				id_bw.flush();
				pw_bw.flush();
				id_bw.close();
				pw_bw.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
		}
		
		//소켓으로 id, name, dept_name, pos를 받고 확인하여 조건문
		try {
			receivedInfo = info_br.readLine();
			String receivedInfo2[] = receivedInfo.split("@");
			userId = receivedInfo2[0];
			userName = receivedInfo2[1];
			deptName = receivedInfo2[2];
			pos = receivedInfo2[3];
			
			//로그인 실패 flag: -1
			if(userId.equals("-1")) {
				new LoginFailWindow();
				
			}
			//로그인 성공
			else {
				//화면전환
				dispose();
				new SelectChatWindow(userId,userName,deptName,pos);
				System.out.println("Login Success");
			}
			//비밀번호 지우기
			passwordField.setText("");
		}catch (IOException e1) {
			try {
				id_bw.flush();
				pw_bw.flush();
				id_bw.close();
				pw_bw.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
		}
		
	}
	
	//엔터 누르면 로그인  이벤트
	public class loginWithEnterAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                login();
            }
        }
	}
}
