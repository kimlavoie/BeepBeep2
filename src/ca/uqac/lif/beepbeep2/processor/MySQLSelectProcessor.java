package ca.uqac.lif.beepbeep2.processor;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

public class MySQLSelectProcessor extends Processor {
	
	private Connection conn;
	private String driver = "com.mysql.jdbc.Driver";
	private String url;
	private String config = "table.txt";
	
	private String ip;
	private String database;
	private String user;
	private String pass;
	private String table;
	private String orderby;
	private HashMap<String,String> columns = new HashMap<String,String>();

	public MySQLSelectProcessor() {
		// TODO Auto-generated constructor stub
	}

	public MySQLSelectProcessor(List<Pipe> inputs, List<Pipe> outputs) {
		super(inputs, outputs);
		// TODO Auto-generated constructor stub
	}

	public MySQLSelectProcessor(Pipe input, Pipe output) {
		super(input, output);
		// TODO Auto-generated constructor stub
		
		try{
			File f = new File(config);
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				Scanner sc2 = new Scanner(line);
				sc2.useDelimiter(":");
				String n = sc2.next();
				String v = sc2.next();
				assignString(n,v);
			}
			
		}
		catch(Exception e) {
			System.out.println("No file");
		}
		
		try{
			url = "jdbc:mysql://"+ip+"/"+database;
			//url = "jdbc:mysql://104.131.187.142/omnimed";
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, user, pass);
		}
		catch (Exception e) {
			System.out.println(e);
		}
		
	}
	private void assignString(String name, String value) {
		if (name.equals("ip") && ip == null)
			ip = value;
		else if(name.equals("database") && database == null)
			database = value;
		else if(name.equals("table") && table == null)
			table = value;
		else if (name.equals("user") && user == null)
			user = value;
		else if (name.equals("pass") && pass == null)
			pass = value;
		else if (name.equals("orderby") && orderby == null)
			orderby = value;
		else if (name.equals("column")) {
			Scanner sc = new Scanner(value);
			sc.useDelimiter(",");
			String v1 = sc.next();
			String v2 = sc.next();
			columns.put(v1, v2);
		}
			
		
	}
	
	private String createQuery() {
		String query = "";
		/*for (Iterator<String> i = columns.iterator(); i.hasNext(); ) {
			query += table+"."+i.next();
			if (i.hasNext())
				query += ",";
		}*/
		query = "SELECT * FROM "+table+" ORDER BY "+orderby+" ASC;";
		return query;
	}
	
	private String getRow(ResultSet rs) throws SQLException {
		String result = "";
		for(Entry<String,String> entry : columns.entrySet()) {
			//System.out.println(entry.getKey()+" "+ entry.getValue());
			if (!result.equals(""))
				result += ",";
			
			if (entry.getValue().equals("int")){
				int i = rs.getInt(entry.getKey());
				result += i;
			}
			else if (entry.getValue().equals("varchar")){
				String i = rs.getString(entry.getKey());
				result += i;
			}
			
		}
		return result;
	}

	@Override
	public void run(){
		// TODO Auto-generated method stub
		while(true){
			try{
				
				String query = createQuery();
				System.out.println(query);
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery(query);
				while(rs.next())
				{
					write(getRow(rs));
				}

			}
			catch(Exception e){
				System.out.println("Query Exception");
			}
		}
		
		
		
	}

}
