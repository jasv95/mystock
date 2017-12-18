import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAO {
	
	public static ArrayList<ManagerDetails> getManagerList(){
		ArrayList<ManagerDetails> md = new ArrayList<ManagerDetails>();
		try {
			Connection con = DataConnect.getConnection();
			// Get a prepared SQL statement
		
						String sql = "SELECT * from users natural join manager_detail where role = ? and status is null";
						PreparedStatement st = con.prepareStatement(sql);
						st.setString(1, "manager");
						st.setInt(2,0);;
						// Execute the statement
						ResultSet rs = st.executeQuery();

						while(rs.next()) {
							md.add(new ManagerDetails(
									rs.getString("user_name"),
									rs.getString("email"),
									rs.getString("name"),
									rs.getDouble("balance"),
									rs.getDouble("fees"),
									rs.getInt("user_id")));
						}
						DataConnect.close();
						return md;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
	public static class ManagerDetails{
		private String user_name,email,name;
		private double fees,balance;
		int user_id;
		public ManagerDetails(String user_name, String email, String name, double balance, double fees, int user_id) {
			this.user_name = user_name;
			this.email = email;
			this.name = name;
			this.balance = balance;
			this.fees = fees;
			this.user_id = user_id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getFees() {
			return fees;
		}
		public void setFees(double fees) {
			this.fees = fees;
		}
		public double getBalance() {
			return balance;
		}
		public void setBalance(double balance) {
			this.balance = balance;
		}
		public int getUser_id() {
			return user_id;
		}
		public void setUser_id(int user_id) {
			this.user_id = user_id;
		}
		
	}

	public static boolean approveManager(int manager_id) {
		try {
			Connection con = DataConnect.getConnection();
			String sql ="update users set status = ? where user_id=?";
			PreparedStatement st = con.prepareStatement(sql);
			System.out.println(String.valueOf(manager_id)+"iddd");
			st.setInt(1, 1);
			st.setInt(2, manager_id);
			if(st.executeUpdate()>0) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
}
