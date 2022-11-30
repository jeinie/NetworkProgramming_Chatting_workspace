package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import client.ChatPanel.Myaction;
import server.User;

public class LoginPanel extends JPanel {
	/* ��Ʈ��ũ�� ���� */
	private final String ip = "127.0.0.1";
	private final int port = 30000;
	public static Socket socket; // �������
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	public static String userID;
	private CommandController controller;

	private JTextField userName; // user �̸� �Է�
	private LoginFrame lf;
	private String id;
	private StartPanel startPanel;
	final String logo = "src/img/sangsang.png";
	private JButton btnNewButton;

	private JLabel Logo;

	public LoginPanel(LoginFrame loginFrame) {

		this.lf = loginFrame;
		setBackground(new Color(255, 222, 0));
		setLayout(null);
		setSize(400, 600);

		Logo = new JLabel(new ImageIcon(logo));
		Logo.setBounds(0,0, 380, 380);
		Logo.setOpaque(false);
		lf.add(Logo);

		JLabel idLabel = new JLabel("����� �̸� ");
		idLabel.setBounds(130, 300, 120, 30);
		idLabel.setFont(new Font("���� ����", Font.PLAIN, 18));
		lf.add(idLabel);

		userName = new JTextField();
		userName.setBounds(70, 350, 200, 50);
		lf.add(userName);
		
		btnNewButton = new JButton("Ȯ��");
		btnNewButton.setFont(new Font("���� ����", Font.PLAIN, 15));
		btnNewButton.setBounds(280, 350, 70, 50);
		lf.add(btnNewButton);

		start();
	}
	
	public void start() { // �׼��̺�Ʈ ���� �޼ҵ�
		Myaction action = new Myaction();
		btnNewButton.addActionListener(action); // ����Ŭ������ �׼� �����ʸ� ��ӹ��� Ŭ������
		userName.addActionListener(action);
	}

	class Myaction implements ActionListener // ����Ŭ������ �׼� �̺�Ʈ ó�� Ŭ����
	{
		public Myaction() {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// �׼� �̺�Ʈ�� sendBtn�϶� �Ǵ� textField ���� Enter key ġ��
			if (e.getSource() == btnNewButton || e.getSource() == userName) {
				String user_id = userName.getText().trim();
				userID = user_id;

				if (user_id != null) { // �������̵� ��ĭ�̸� �ȵ�
					network();// ������ ����
					MainFrame f = new MainFrame(userID);
					lf.dispose();
				}
			}

		}

	}

	/* ��Ʈ��ũ ���� ���� */

	public void network() {
		try {
			socket = new Socket(ip, port);
			if (socket != null) // socket�� null���� �ƴҶ� ��! ����Ǿ�����
			{
				controller = CommandController.getController();// ��ó��
																// getController�ϴ�
																// �κ�->���⼭ ������
				Connection(); // ���� �޼ҵ带 ȣ��
			}
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			System.out.println("Client ���� ���� ����!!\n");
		}
	}

	public void Connection() { // �������� �޼ҵ� ����κ�
		try { // ��Ʈ�� ����
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
		} catch (IOException e) {
			// textArea.append("��Ʈ�� ���� ����!!\n");
			System.out.println("��Ʈ�� ���� ����!!\n");
		}
		if (controller == null) {
			System.out.println("controller == null");
		} else {
			controller.send_Message(User.SIGNAL_USER_ID + "//" + userID); // ���������� ����Ǹ� ���� ���̵� ����
			//���Ŀ� �ٷ� SIGNAL_ONLINE_USER_LIST�� �����⶧���� ������ ó���� �ð��� �־�� ��.
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	public StartPanel getStartPanel() {
		return startPanel;
	}

	public void setStartPanel(StartPanel startPanel) {
		this.startPanel = startPanel;
	}
}