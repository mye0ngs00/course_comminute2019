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
	
	private static String ipNumber = "10.50.5.255"; // ������ IP 
	private static int portNumber = 3000; // ������ Port ��ȣ
	
	private JPanel contentPane;
	private JPasswordField passwordField;
	private JTextField textField;
	private JPanel inputPanel;
	private JButton loginBtn;
	private JButton forgotBtn;
	private Image logo_img = null;
	private Image login_img = null;
	//�α��� �г�
	private JPanel loginPanel;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel2;
	//�ΰ��̹��� �ҽ��ڵ�
	private File srcimg;
	private File srcimg2;
	
	//id,pw ����
	private static Socket idSocket = null;
	private static Socket pwSocket = null;
	private static volatile BufferedWriter id_bw;
	private static volatile BufferedWriter pw_bw;
	
	//����
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
				System.out.println("Server IP, Port �Է� �ٶ��ϴ�.");
				System.out.println("�߸��� �����̿��� ���α׷��� �����մϴ�.");
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
		
		//ȭ�� �߾� ��ġ
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������

		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
		//this.setUndecorated(true); //â ������ ���ֱ�. visible���� �ؾ���.
		this.setResizable(false); //â ũ�� ���� �Ұ���
		
		//�� ó�� �г�
		loginPanel = new JPanel();
		loginPanel.setBounds(0, 0, 601, 565);
		contentPane.add(loginPanel);
		loginPanel.setLayout(null);
		
		//�α��� ��ư �̺�Ʈ
		loginBtn = new JButton("");
		//��ư �����ϰ�
		loginBtn.setBackground(Color.white);
		loginBtn.setOpaque(false);
		loginBtn.setFocusPainted(true);
		loginBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //�׵θ� ���ֱ�
		loginBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//�α���
				login();
			}
		});
		
		
		loginBtn.setBounds(87, 346, 421, 62);
		loginPanel.add(loginBtn);
		
		//��й�ȣ ã�� ��ư �̺�Ʈ
		forgotBtn = new JButton("");
		forgotBtn.setLocation(195, 464);
		forgotBtn.setSize(216, 44);
		forgotBtn.setBackground(Color.white);
		forgotBtn.setOpaque(false);
		forgotBtn.setFocusPainted(true);
		forgotBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //�׵θ� ���ֱ�
		forgotBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//��й�ȣ ã��
				new FindPassword();
			}
		});
		loginPanel.add(forgotBtn);
		
		//�̹��� �ҷ�����
		srcimg = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\logo.png"); //��ܹ��
		srcimg2 = new File("C:\\Users\\myeongsoo\\Desktop\\resources\\loginform.png"); //�α��� ��
		try {
			logo_img = ImageIO.read(srcimg).getScaledInstance(330, 70, java.awt.Image.SCALE_SMOOTH);
			login_img = ImageIO.read(srcimg2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//��ܹ��
		lblNewLabel = new JLabel(new ImageIcon(logo_img));
		lblNewLabel.setBounds(132, 76, 330, 70);
		loginPanel.add(lblNewLabel);
		
		lblNewLabel2 = new JLabel(new ImageIcon(login_img));
		lblNewLabel2.setBounds(0, 0, 601, 567);
		loginPanel.add(lblNewLabel2);
		
		
		//���̵� �Է�
		textField = new JTextField();
		textField.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //�׵θ� ���ֱ�
		textField.setBounds(132, 185, 369, 53);
		loginPanel.add(textField);
		textField.setColumns(10);
		
		//��й�ȣ �Է�
		passwordField = new JPasswordField();
		passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder()); //�׵θ� ���ֱ�
		passwordField.setBounds(132, 267, 369, 53);
		loginPanel.add(passwordField);
		passwordField.addKeyListener(new loginWithEnterAdapter());
		
		//���ͷ� �α���
		textField.addKeyListener(new loginWithEnterAdapter());
		
	}
	//�α��� �õ�. �������� id/pw ����
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
		
		//�������� id, name, dept_name, pos�� �ް� Ȯ���Ͽ� ���ǹ�
		try {
			receivedInfo = info_br.readLine();
			String receivedInfo2[] = receivedInfo.split("@");
			userId = receivedInfo2[0];
			userName = receivedInfo2[1];
			deptName = receivedInfo2[2];
			pos = receivedInfo2[3];
			
			//�α��� ���� flag: -1
			if(userId.equals("-1")) {
				new LoginFailWindow();
				
			}
			//�α��� ����
			else {
				//ȭ����ȯ
				dispose();
				new SelectChatWindow(userId,userName,deptName,pos);
				System.out.println("Login Success");
			}
			//��й�ȣ �����
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
	
	//���� ������ �α���  �̺�Ʈ
	public class loginWithEnterAdapter extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                login();
            }
        }
	}
}
