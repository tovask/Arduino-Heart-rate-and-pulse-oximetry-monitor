
/*
 * http://hsqldb.org/
 * https://docs.oracle.com/javase/7/docs/api/java/sql/DriverManager.html
 * 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Random;

// javac Database.java && java -cp hsqldb-2.3.6.jar:. Database
// javac database.java && jar cvfm PC.jar PC.mf database.class -C ~/ideg hsqldb-2.3.6.jar && java -jar PC.jar
// jar cvfm ~/Arduino/Uno_hearth_rate_stream/pc/PC.* -C ~/Arduino/Uno_hearth_rate_stream/pc database.class -C ~/ideg/hsqldb-2.3.4/hsqldb/src/ org
// find . -type f -name "*.java" -exec javac {} \;

public class Database {
	
	String driver = "org.hsqldb.jdbc.JDBCDriver";
	String dbfile = "data";
	String url = "jdbc:hsqldb:file:" + dbfile;
	String username = "SA";
	String password = "";
	String tableName = "data".toUpperCase();
	
	Connection con = null;
	
	public Database() throws ClassNotFoundException, SQLException {
		//Registering the HSQLDB JDBC driver
		Class.forName(driver);
		//Creating the connection with HSQLDB
		con = DriverManager.getConnection(url, username, password);
		if (con == null || con.isClosed() || !con.isValid(0)) {
			System.out.println("Connecting to the database failed!!!");
			return;
		}
		System.out.println("Connection to the database created successfully");
		
		// con.createStatement().execute("DROP TABLE IF EXISTS DATA;");
		// check if table is there
		// TODO use: CREATE TABLE IF NOT EXISTS
		ResultSet rs = con.getMetaData().getTables(null, null, tableName, null);
		if (rs.next() && rs.getString("TABLE_NAME")!=null && rs.getString("TABLE_NAME").equals(tableName)) {
			System.out.println("Database table already exists.");
		} else {
			System.out.println("Database table not exists yet, creating it...");
			con.prepareStatement(
				"CREATE TABLE " + tableName + " ( " +
					"id INTEGER IDENTITY PRIMARY KEY, "+
					"ecg INTEGER, " + 
					"po_IR INTEGER, " + 
					"po_Red INTEGER, " + 
					"timestamp TIMESTAMP " + 
				" );")
				.execute();
			// con.prepareStatement("SET TABLE " + tableName + " TYPE CACHED;").execute();
		}
		con.prepareStatement("SET FILES WRITE DELAY FALSE;").execute();
	}
	
	public void store(int ecg, int po_IR, int po_Red) {
		PreparedStatement pstm = null;
		try {
			pstm = con.prepareStatement(
				"INSERT INTO " + tableName + " (ecg, po_IR, po_Red, timestamp) "+
				"VALUES (?, ?, ?, CURRENT_TIMESTAMP);");
			pstm.setInt( 1, ecg);
			pstm.setInt( 2, po_IR);
			pstm.setInt( 3, po_Red);
			int count = pstm.executeUpdate();
			pstm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printAll(){
		try {
			String sql = "SELECT * FROM " + tableName;
			System.out.println(sql);
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(sql);
			while(rs.next()){
				System.out.print("\tid: " + rs.getInt("id"));
				System.out.print(",   \tecg: " + rs.getInt("ecg"));
				System.out.print(",\tpo_IR: " + rs.getInt("po_IR"));
				System.out.print(",\tpo_Red: " + rs.getInt("po_Red"));
				System.out.print(",\ttimestamp: " + rs.getTimestamp("timestamp"));
				System.out.println();
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void printCount(){
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM " + tableName);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while(rs.next()){
				System.out.print("Records count: " + rs.getInt(1));
				System.out.println();
			}
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Random rand = new Random();
		try {
			Database db = new Database();
			// System.out.println("Inserting some random values for testing...");
			// db.store( rand.nextInt(50000), rand.nextInt(50000), rand.nextInt(50000));
			// db.store( rand.nextInt(50000), rand.nextInt(50000), rand.nextInt(50000));
			// db.store( rand.nextInt(50000), rand.nextInt(50000), rand.nextInt(50000));
			db.printAll();
			db.printCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	private void sdvjhfvbdajhvba(){
		String dbfile = "testdb";
		
      Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
      
      try {
		  //Registering the HSQLDB JDBC driver
		  Class.forName(driver);
		  //Creating the connection with HSQLDB
		  con = DriverManager.getConnection(url, username, password);
		  if (con != null && !con.isClosed() && con.isValid(0)) {
			  System.out.println("Connection to the database created successfully");
			  // TODO create db/table if not exists
			  // con.prepareStatement("DROP TABLE IF EXISTS testtable;")
			  //         .execute();
			  // con.prepareStatement(
			  //         "CREATE TABLE testtable ( id INTEGER, "+
			  //         "name VARCHAR(255) );")
			  //         .execute();
			con.prepareStatement(
                    "INSERT INTO testtable(id, name) "+
                    "VALUES (1, 'testvalue');")
                    .execute();
			
			// Writing a query
            String sql = "SELECT id, name FROM testtable";

            // Setting up the SQL statement
            pstm = con.prepareStatement(sql);

            // Execute the SQL query
            rs = pstm.executeQuery();

            // Iterating the results
            while (rs.next()) {
                // Simple method to show the data
                System.out.print(rs.getInt("id") + ", ");
                System.out.print(rs.getString("name") +  ", ");
                // System.out.print(rs.getDate("hiredate") + ", ");
                System.out.println();
            }
         }else{
            System.out.println("Problem with creating connection");
         }
      
      }  catch (Exception e) {
         e.printStackTrace();
		 throw new RuntimeException(e);
      } finally {
            try {
                // Closing all the opened resources
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
                if (con != null) con.close();
            } catch(final Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
   }
   */
}
