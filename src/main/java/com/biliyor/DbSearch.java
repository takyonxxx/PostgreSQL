package com.biliyor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.*;

public class DbSearch extends JPanel {

	private DbTable dbTable;	
	private static DbSearch instance;

	public DbSearch() {

		JLabel label = new JLabel("Filter record:");;
		JTextField textSearch = new JTextField();
		JCheckBox caseCheckBox = new JCheckBox("From Name");
		JCheckBox wrapCheckBox = new JCheckBox("From Date");
		JCheckBox wholeCheckBox = new JCheckBox("From Action");

		JButton findButton = new JButton("Find");
		JButton exitButton = new JButton("Exit");

		dbTable = DbTable.getInstance();

		exitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// button search row
		findButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) { 			

				dbTable.searchDb(textSearch.getText());				
			}
		});

		// remove redundant default border of check boxes - they would hinder
		// correct spacing and aligning (maybe not needed on some look and feels)
		caseCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		wrapCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		wholeCheckBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(label)
				.addGroup(layout.createParallelGroup(LEADING)
						.addComponent(textSearch)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(LEADING)
										.addComponent(caseCheckBox)
										.addComponent(wholeCheckBox))
								.addGroup(layout.createParallelGroup(LEADING)
										.addComponent(wrapCheckBox)
										)))
				.addGroup(layout.createParallelGroup(LEADING)
						.addComponent(findButton)
						.addComponent(exitButton))
				);

		layout.linkSize(SwingConstants.HORIZONTAL, findButton, exitButton);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(BASELINE)
						.addComponent(label)
						.addComponent(textSearch)
						.addComponent(findButton))
				.addGroup(layout.createParallelGroup(LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(BASELINE)
										.addComponent(caseCheckBox)
										.addComponent(wrapCheckBox))
								.addGroup(layout.createParallelGroup(BASELINE)
										.addComponent(wholeCheckBox)
										))
						.addComponent(exitButton))
				);
	}

	public static DbSearch getInstance() {

		if (instance == null) {
			instance = new DbSearch();
		} 
		return instance;
	}

}
