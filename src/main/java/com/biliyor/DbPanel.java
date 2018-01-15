package com.biliyor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class DbPanel extends JPanel {	

	private static final long serialVersionUID = 1L;
	private JTextField textId;
	private JTextField textName;
	private JTextField textDate;
	private JTextField textAction;
	private JTextField textSearch;

	private JLabel lId;
	private JLabel lName;
	private JLabel lDate;
	private JLabel lAction;
	private JLabel lSearch;

	private JButton btnAdd;
	private JButton btnDelete;
	private JButton btnUpdate;
	private JButton btnSearch;

	private DbTable dbTable;
	private ToolBar toolBar;	
	
	private static DbPanel instance;
	private DbListener dbListener;
	
	
	public DbPanel() {
	
		setLayout(new BorderLayout());	

		toolBar = ToolBar.getInstance();	
		toolBar.setBounds(10,10,880,30);				
		add(toolBar,BorderLayout.NORTH);		

		dbTable = new DbTable();	
				
		JScrollPane sTable = new JScrollPane(dbTable);
		
		dbTable.searchDb("");
		dbTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// get selected row data From table to textfields 
		dbTable.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){

				// i = the index of the selected row
				int i = dbTable.getSelectedRow();

				textId.setText(dbTable.model.getValueAt(i, 0).toString());
				textName.setText(dbTable.model.getValueAt(i, 1).toString());
				textDate.setText(dbTable.model.getValueAt(i, 2).toString());
				textAction.setText(dbTable.model.getValueAt(i, 3).toString());
			}
		});		

		Border innerBorder = BorderFactory.createTitledBorder("Manage Postgresql Database");
		Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);		
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		add(initButtons(),BorderLayout.WEST);
		add(sTable,BorderLayout.CENTER);		

		// create an array of objects to set the row data
		final Object[] row = new Object[4];

		// button add row
		btnAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dbListener.setValues(textId.getText(), textName.getText(), textDate.getText(), textAction.getText());
				dbTable.addDb();
			}
		});

		// button remove row
		btnDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dbListener.setValues(textId.getText(), textName.getText(), textDate.getText(), textAction.getText());
				dbTable.deleteDb();
			}
		});				

		// button update row
		btnUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dbListener.setValues(textId.getText(), textName.getText(), textDate.getText(), textAction.getText());
				dbTable.updateDb();
			}
		});

		// button search row
		btnSearch.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) { 				
				dbListener.setValues(textId.getText(), textName.getText(), textDate.getText(), textAction.getText());
				dbTable.searchDb(textSearch.getText());				
			}
		});

	}
	
	public void setDbListener(DbListener listener)
	{
		this.dbListener = listener;
	}
	
    public static DbPanel getInstance() {
		
		if (instance == null) {
			instance = new DbPanel();
		} 
		return instance;
	}
			
	private JPanel initButtons() {

		JPanel panel = new JPanel();

		// create JLabels
		lId = new JLabel("Id:");   
		lName = new JLabel("Name:");   
		lDate = new JLabel("Date:");   
		lAction = new JLabel("Action:");   
		lSearch = new JLabel("Search:");   

		// create JTextFields
		textId = new JTextField(15);
		textName = new JTextField(15);
		textDate = new JTextField(15);
		textAction = new JTextField(15);
		textSearch = new JTextField(15);

		// create JButtons
		btnAdd = new JButton("Add");
		btnDelete = new JButton("Delete");
		btnUpdate = new JButton("Update");   		
		btnSearch = new JButton("Search");   

		lSearch.setForeground(Color.blue);

		panel.setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();

		////first row

		gc.weightx = 1;
		gc.weighty = 0.1;		

		gc.gridx = 0;
		gc.gridy = 0;

		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.LINE_END;	
		gc.insets = new Insets(0,0,0,5);
		panel.add(lId,gc);		

		gc.gridx = 1;
		gc.gridy = 0;	 
		gc.anchor = GridBagConstraints.LINE_START;	
		gc.insets = new Insets(0,0,0,5);
		panel.add(textId,gc);

		///second row

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 1;
		gc.gridx = 0;	
		gc.anchor = GridBagConstraints.LINE_END;	
		gc.insets = new Insets(0,0,0,5);
		panel.add(lName,gc);

		gc.gridy = 1;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(textName,gc);	    

		///third row

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 2;
		gc.gridx =0;	
		gc.anchor = GridBagConstraints.LINE_END;	
		gc.insets = new Insets(0,0,0,5);
		panel.add(lDate,gc);

		gc.gridy = 2;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(textDate,gc);	    

		///forth row

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 3;
		gc.gridx = 0;	
		gc.anchor = GridBagConstraints.LINE_END;	
		gc.insets = new Insets(0,0,0,5);
		panel.add(lAction,gc);

		gc.gridy = 3;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.LINE_START;		
		gc.insets = new Insets(0,0,0,0);
		panel.add(textAction,gc);	  		


		///first button

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 4;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(btnAdd,gc);

		///second button

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 5;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(btnDelete,gc);

		///third button

		gc.weightx = 1;
		gc.weighty = 1.5;

		gc.gridy = 6;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(btnUpdate,gc);

		///search text
		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 7;
		gc.gridx = 0;	
		gc.anchor = GridBagConstraints.LINE_END;	
		gc.insets = new Insets(0,0,0,5);
		panel.add(lSearch,gc);

		gc.gridy = 7;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(textSearch,gc);	    

		///search button

		gc.weightx = 1;
		gc.weighty = 10;

		gc.gridy = 8;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(btnSearch,gc);

		return panel;
	}

}
