import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoginFailWindow extends JFrame{
    LoginFailWindow() {
        setTitle("�α���");
        // ����, ���⼭ setDefaultCloseOperation() ���ǽ� X������ ��� â�� ����
        
        JPanel NewWindowContainer = new JPanel();
        setContentPane(NewWindowContainer);
        NewWindowContainer.setLayout(null);
        
        JLabel NewLabel = new JLabel("��й�ȣ�� �ٽ� Ȯ�� ���ּ���");
        NewLabel.setBounds(17, 15, 252, 21);
	        
        NewWindowContainer.add(NewLabel);
        //ȭ�� �߾� ��ġ
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������
	
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
        setSize(296,100);
        setResizable(false);
        setVisible(true);
    }
}
