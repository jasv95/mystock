import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;


public class AccountDAO {

	public static Double getAccBalance(int user_id) {
		Connection con = null;
		try {
			// Setup the DataSource object

			con = DataConnect.getConnection();

			// Get a prepared SQL statement
			String sql = "SELECT balance from users where user_id = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_id);
			// Execute the statement
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				String balance = rs.getString(1);
				DataConnect.close();
				return Double.parseDouble(balance);
			} else {

				DataConnect.close();
				return null;
			}

		} catch (Exception e) {

		}
		return null;

	}
	
	public static boolean setAccBalance(int user_id,double balance) {
		Connection con = null;
		try {
			// Setup the DataSource object

			con = DataConnect.getConnection();

			// Get a prepared SQL statement
			String sql = "update  users set balance=? where user_id = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setDouble(1, balance);
			st.setInt(2, user_id);
			// Execute the statement
			if (st.executeUpdate() > 0) {
				DataConnect.close();
				return true;
			}
			 else {

				DataConnect.close();
				return false;
			}

		} catch (Exception e) {

		}
		return false;

	}

	public static ResultSet getActivities(int user_id){


			try {
				Connection con = DataConnect.getConnection();
				String sql = "Select * from acc_activity where user_id= ?";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, user_id);
				ResultSet rs = st.executeQuery();
				return rs;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		return null;
	}

	public static ResultSet getHoldings(int user_id) {
		try {
			Connection con = DataConnect.getConnection();
			String sql = "Select * from holdings where user_id= ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_id);
			ResultSet rs = st.executeQuery();
			return rs;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	return null;
	}
}
