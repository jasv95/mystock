
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DataConnect {

	public static String hostName = "localhost";
	public static String portNumber = "3306";
	public static String databaseName = "mystock";
	public static String userName = "root";
	public static String password = "root";
	private static Connection con;

	private DataConnect() {
		
	}
	
	public static Connection getConnection() throws SQLException {

		hostName = System.getenv("ICSI518_SERVER");
		portNumber = System.getenv("ICSI518_PORT");
		databaseName = System.getenv("ICSI518_DB");
		userName = System.getenv("ICSI518_USER");
		password = System.getenv("ICSI518_PASSWORD");
		
		if (con == null || con.isClosed()) {
			try {
				System.out.println("inside getConnection");
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://" + hostName + ":" + portNumber + "/" + databaseName,
						userName, password);
			} catch (Exception ex) {
				System.out.println("Database.getConnection() Error -->" + ex.getMessage());
				return null;
			}
		}
		return con;
	}

	public static void close() {
		try {
			con.close();
			con = null;
		} catch (Exception ex) {
		}
	}
}
