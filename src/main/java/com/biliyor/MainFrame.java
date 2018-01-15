package com.biliyor;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame{	
	
	ToolBar toolBar;
	DbPanel dbPanel;
	
	public MainFrame() {

		super("Main Database Window");
		setLayout(new BorderLayout());	

		//setLayout(null);
		setSize(900,450);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

		dbPanel = DbPanel.getInstance();			

		add(dbPanel,BorderLayout.CENTER);
		
		setVisible(true);
	}		
	
}

