package com.biliyor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ToolBar extends JPanel implements ActionListener{
	
	private JButton exitButton;
	private JButton connectButton;
	private JButton disconnectButton;
	private DbListener dbListener;
	private JLabel lStatus;
	private static ToolBar instance;
	
	public ToolBar() {
		
		connectButton = new JButton("Connect");	
		disconnectButton = new JButton("Disconnect");	
		exitButton = new JButton("Exit");		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setBorder(BorderFactory.createEtchedBorder());
		
		lStatus = new JLabel("");   
		
		add(connectButton);
		add(disconnectButton);
		add(exitButton);
		add(lStatus);
		
		connectButton.addActionListener(this);
		disconnectButton.addActionListener(this);		
		exitButton.addActionListener(this);
		
		disconnectButton.setEnabled(true);
		connectButton.setEnabled(false);
		
	}
	
	public static ToolBar getInstance() {
		
		if (instance == null) {
			instance = new ToolBar();
		} 
		return instance;
	}
	
	public void setDbListener(DbListener listener)
	{
		this.dbListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		JButton clicked = (JButton) e.getSource();
		
		if(clicked == exitButton)
		{			
			dbListener.setConnection(0);
			System.exit(0);
		}
		else if(clicked == connectButton)
		{			
			dbListener.setConnection(1);
			disconnectButton.setEnabled(true);
			connectButton.setEnabled(false);			
		}
		else if(clicked == disconnectButton)
		{			
			dbListener.setConnection(0);
			disconnectButton.setEnabled(false);
			connectButton.setEnabled(true);			
		}
	}

	
	public String getlStatus() {
		return lStatus.getText();
	}

	
	public void setlStatus(String lStatus) {
		this.lStatus.setText(lStatus);
	}

}
