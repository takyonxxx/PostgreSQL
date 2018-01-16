package com.biliyor;
import java.awt.BorderLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame{	

	ToolBar toolBar;
	DbPanel dbPanel;
	DbSearch groupFrame;

	public MainFrame() {

		super("Main Database Window");
		setLayout(new BorderLayout());	

		//setLayout(null);
		setSize(800,480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		

		dbPanel = DbPanel.getInstance();	
		groupFrame = new DbSearch();

		add(dbPanel,BorderLayout.CENTER);
		add(groupFrame,BorderLayout.SOUTH);

		setVisible(true);
	}		

}

