package com.biliyor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class DbPanel extends JPanel {	

	private static final long serialVersionUID = 1L;
	private JTextField textId;
	private JTextField textName;
	private JTextField textDate;
	private JTextField textAction;

	private JLabel lId;
	private JLabel lName;
	private JLabel lDate;
	private JLabel lAction;
	private JLabel lImageName;
	private JLabel lImage;

	private JButton btnAdd;
	private JButton btnDelete;
	private JButton btnUpdate;
	private JButton btnImage;

	JFileChooser fc;

	private DbTable dbTable;	
	private ToolBar toolBar;	

	private static DbPanel instance;
	private DbListener dbListener;

	private String imageName;
	private byte[] imageBytes = null;		

	protected void paintComponent(Graphics gc) {
		super.paintComponent(gc);
		Dimension cs=getSize();                           // component size
		gc=gc.create();	   
	}

	public static boolean isEmpty(JTable jTable) {
		if (jTable != null && jTable.getModel() != null) {
			return jTable.getModel().getRowCount()<=0?true:false;
		}
		return false;
	}

	public DbPanel() {

		setLayout(new BorderLayout());			

		toolBar = ToolBar.getInstance();	
		toolBar.setBounds(10,10,880,30);				
		add(toolBar,BorderLayout.NORTH);		

		dbTable = DbTable.getInstance();

		JScrollPane sTable = new JScrollPane(dbTable);

		dbTable.searchDb("");
		dbTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);	

		Border innerBorder = BorderFactory.createTitledBorder("Manage Postgresql Database");
		Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);		
		setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

		add(initButtons(),BorderLayout.WEST);
		add(sTable,BorderLayout.CENTER);		

		ListSelectionModel cellSelectionModel = dbTable.getSelectionModel();

		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {     

				if (listSelectionEvent.getValueIsAdjusting() || isEmpty(dbTable))
					return;
				ListSelectionModel lsm = (ListSelectionModel)listSelectionEvent.getSource();
				if (lsm.isSelectionEmpty()) {
					System.out.println("No rows selected");
				}
				else{
					try {
						int i = lsm.getMinSelectionIndex();
						textId.setText(dbTable.model.getValueAt(i, 0).toString());
						textDate.setText(dbTable.model.getValueAt(i, 1).toString());
						textName.setText(dbTable.model.getValueAt(i, 2).toString());				
						textAction.setText(dbTable.model.getValueAt(i, 3).toString());
						lImageName.setText(dbTable.model.getValueAt(i, 4).toString());
						imageBytes =  (byte[]) dbTable.model.getValueAt(i, 5);					
						Image img = Toolkit.getDefaultToolkit().createImage(imageBytes);
						Image dimg = img.getScaledInstance(160,90,Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(dimg);				
						lImage.setIcon(icon);
					}
					catch (Exception e){
						System.out.println(e.getMessage());
						lImage.setIcon(null);
					}
				}
			}

		});


		if(!isEmpty(dbTable))
		{
			// This will set the selection to the first row
			dbTable.setRowSelectionInterval(0, 0);			
		}

		// button add row
		btnAdd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dbListener.setValues(textId.getText(), textName.getText(), imageName, imageBytes, textDate.getText(), textAction.getText());
				dbTable.addDb();
			}
		});

		// button remove row
		btnDelete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dbListener.setValues(textId.getText(), textName.getText(), imageName, imageBytes, textDate.getText(), textAction.getText());
				dbTable.deleteDb();
			}
		});				

		// button update row
		btnUpdate.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dbListener.setValues(textId.getText(), textName.getText(),imageName, imageBytes, textDate.getText(), textAction.getText());
				dbTable.updateDb();
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
		lImageName = new JLabel("No Image"); 
		lImage = new JLabel(""); 

		// create JTextFields
		textId = new JTextField(15);
		textName = new JTextField(15);
		textDate = new JTextField(15);
		textAction = new JTextField(15);	

		//Create a file chooser
		fc = new JFileChooser(); 
		btnImage = new JButton("Chose Image");

		btnImage.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				int returnVal = fc.showOpenDialog(DbPanel.this);

				if (returnVal == JFileChooser.APPROVE_OPTION) {					

					BufferedImage img;					
					try {
						File file = fc.getSelectedFile();
						FileInputStream fis = new FileInputStream(file);

						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte[] bytes = new byte[1024];
						for(int len; (len = fis.read(bytes))>0;)
							baos.write(bytes, 0, len);

						imageBytes = baos.toByteArray();

						imageName = file.getName();
						//This is where a real application would open the file.
						lImageName.setText(imageName);		

						img = ImageIO.read(fc.getSelectedFile());
						Image dimg = img.getScaledInstance(160,90,Image.SCALE_SMOOTH);
						ImageIcon icon = new ImageIcon(dimg);				
						lImage.setIcon(icon);

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else {	                
					toolBar.setlStatus("Open command cancelled by user.");
				}	         
			}

		});

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		String sDate = dateFormat.format(date);
		textDate.setText(sDate);


		try {
			Image img = ImageIO.read(getClass().getResource("/new.png"));
			Image scaleImage = img.getScaledInstance(47, 18, Image.SCALE_DEFAULT);		    
			btnAdd = new JButton(new ImageIcon(scaleImage));
		} catch (Exception ex) {
			System.out.println(ex);
		}		

		// create JButtons

		btnDelete = new JButton("Delete");
		btnUpdate = new JButton("Update");   		

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
		gc.weighty = 0.1;

		gc.gridy = 6;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(btnUpdate,gc);		

		///fourth button

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 7;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(btnImage,gc);		

		///label image name

		gc.weightx = 1;
		gc.weighty = 0.1;

		gc.gridy = 8;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(lImageName,gc);		

		///label image

		gc.weightx = 1;
		gc.weighty = 2;

		gc.gridy = 9;
		gc.gridx = 1;	
		gc.anchor = GridBagConstraints.FIRST_LINE_START;		
		gc.insets = new Insets(0,0,0,5);
		panel.add(lImage,gc);		

		return panel;
	}

}
