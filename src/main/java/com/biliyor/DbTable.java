package com.biliyor;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DbTable extends JTable {

	DefaultTableModel model = null;	
	private PreparedStatement pst = null;
	private ResultSet rs = null;   
	private Connection con = null;
	private ToolBar toolBar;	
	private DbPanel dbPanel;
	
	private String id;
	private String name;
	private String sdate;
	private String action;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}	


	public DbTable()
	{
		// Change A JTable Background Color, Font Size, Font Color, Row Height
		setBackground(Color.LIGHT_GRAY);
		setForeground(Color.black);
		Font font = new Font("",1,18);
		setFont(font);
		setRowHeight(25);		

		EventQueue.invokeLater(new Runnable() {
			public void run() {				 
				try {
					
					dbPanel = DbPanel.getInstance();	
					dbPanel.setDbListener(new DbListener() {
						private String id;

						@Override
						public void setConnection(int cmd) {

						}

						@Override
						public void setValues(String id, String name, String date, String action) {
							setId(id);
							setName(name);
							setSdate(date);
							setAction(action);
						}
					});								
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
		
		toolBar = ToolBar.getInstance();
		toolBar.setDbListener(new DbListener() {
			@Override
			public void setConnection(int cmd) {
				// TODO Auto-generated method stub

				if(cmd == 0)
				{
					disconnectDb();
				}
				else if(cmd == 1)
				{
					connectDb();
				}
			}

			@Override
			public void setValues(String id, String name, String date, String action) {
				// TODO Auto-generated method stub

			}
		});	
		
		connectDb();			

	}

	void connectDb()
	{
		try {
			if (con == null) {
				DatabaseConnection dbConn = DatabaseConnection.getInstance();
				con = dbConn.getConnection();	

				toolBar.setlStatus("Database connected...");
			}			

		} catch (SQLException e) {

			Logger lgr = Logger.getLogger(MainFrame.class.getName());
			lgr.log(Level.WARNING, e.getMessage(), e);
		}
	}

	void disconnectDb()
	{
		try {

			if (con != null) {
				con.close();
				con = null;

				toolBar.setlStatus("Database disconnected...");
			}	

		} catch (SQLException ex) {

			Logger lgr = Logger.getLogger(MainFrame.class.getName());
			lgr.log(Level.WARNING, ex.getMessage(), ex);
		}
	}	


	public void searchDb(String str)
	{

		String query = "SELECT person.id,name,date,dest From person, " +
				"actions WHERE (person.id = actions.person_id AND person.name  SIMILAR TO '%(" +
				str +
				")%') ORDER BY id ASC ";

		try {

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			resultSetToTableModel(rs);	

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(App.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}	     
	}

	public void addDb()
	{	
		try {

			int id = Integer.parseInt(this.id);
			int person_id = id;				

			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
			java.time.LocalDate textFieldAsDate = java.time.LocalDate.parse(this.sdate, formatter);
			java.sql.Date sqlDate = java.sql.Date.valueOf(textFieldAsDate);

			String stm = "INSERT INTO person(id, name) VALUES(?, ?)";
			pst = con.prepareStatement(stm);
			pst.setInt(1, id);
			pst.setString(2, this.name);    			
			pst.executeUpdate();

			stm = "INSERT INTO actions(id, person_id, date,dest) VALUES(?, ?, ?, ?)";
			pst = con.prepareStatement(stm);
			pst.setInt(1, id);
			pst.setInt(2, person_id);    
			pst.setDate(3, sqlDate);  
			pst.setString(4, this.action);
			pst.executeUpdate();

			searchDb("");

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(App.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	public void deleteDb()
	{
		try {	
			
			int id = Integer.parseInt(this.id);

			String SQL = "DELETE FROM actions WHERE person_id = ?";	
			pst = con.prepareStatement(SQL);
			pst.setInt(1, id);
		    pst.executeUpdate();		 

			SQL = "DELETE FROM person WHERE id = ?";
			pst = con.prepareStatement(SQL);
			pst.setInt(1, id);
			pst.executeUpdate();		 

			searchDb("");

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(App.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	public void updateDb()
	{
		try {	

			int id = Integer.parseInt(this.id);
			int person_id = id;				

			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
			java.time.LocalDate textFieldAsDate = java.time.LocalDate.parse(this.sdate, formatter);
			java.sql.Date sqlDate = java.sql.Date.valueOf(textFieldAsDate);

			String stm = "UPDATE person SET name = ?  WHERE id = ?";
			pst = con.prepareStatement(stm);			
			pst.setString(1, this.name);  
			pst.setInt(2, id);
			pst.executeUpdate();

			stm = "UPDATE actions SET date = ? ,dest = ? WHERE person_id = ?";
			pst = con.prepareStatement(stm);
			pst.setDate(1, sqlDate);  
			pst.setString(2, this.action);
			pst.setInt(3, person_id);   			
			pst.executeUpdate();

			searchDb("");

		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(App.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}


	public void resultSetToTableModel(ResultSet rs) throws SQLException{
		//Create new table model
		model  = new DefaultTableModel();	

		//Retrieve meta data from ResultSet
		ResultSetMetaData metaData = rs.getMetaData();

		//Get number of columns from meta data
		int columnCount = metaData.getColumnCount();

		//Get all column names from meta data and add columns to table model
		for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++){
			model.addColumn(metaData.getColumnLabel(columnIndex));
		}

		//Create array of Objects with size of column count from meta data
		Object[] row = new Object[columnCount];

		//Scroll through result set
		while (rs.next()){
			//Get object from column with specific index of result set to array of objects
			for (int i = 0; i < columnCount; i++){
				row[i] = rs.getObject(i+1);
			}
			//Now add row to table model with that array of objects as an argument
			model.addRow(row);
		}

		//Now add that table model to your table and you are done :D
		setModel(model);
		Logger lgr = Logger.getLogger(App.class.getName());
	}	

}
