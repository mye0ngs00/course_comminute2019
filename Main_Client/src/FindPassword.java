import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class FindPassword extends JFrame{
	FindPassword(){
		JPanel NewWindowContainer = new JPanel();
        setContentPane(NewWindowContainer);
        NewWindowContainer.setLayout(new FlowLayout());
        JTextArea idpw = new JTextArea("guest - guest \n root - root \n abc123 - abc123");
        idpw.setEditable(false);
		NewWindowContainer.add(idpw);
		//ȭ�� �߾� ��ġ
		Dimension frameSize = this.getSize(); // ������ ������
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // ����� ������

		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
        setSize(296,100);
        setResizable(false);
        setVisible(true);
	}
}
