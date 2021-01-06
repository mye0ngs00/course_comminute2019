import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoginFailWindow extends JFrame{
    LoginFailWindow() {
        setTitle("로그인");
        // 주의, 여기서 setDefaultCloseOperation() 정의시 X누르면 모든 창이 닫힘
        
        JPanel NewWindowContainer = new JPanel();
        setContentPane(NewWindowContainer);
        NewWindowContainer.setLayout(null);
        
        JLabel NewLabel = new JLabel("비밀번호를 다시 확인 해주세요");
        NewLabel.setBounds(17, 15, 252, 21);
	        
        NewWindowContainer.add(NewLabel);
        //화면 중앙 배치
		Dimension frameSize = this.getSize(); // 프레임 사이즈
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 모니터 사이즈
	
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
        setSize(296,100);
        setResizable(false);
        setVisible(true);
    }
}
