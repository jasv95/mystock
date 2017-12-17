import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class LoginDAO {

	public static String login(String user, String password) {

		Connection con = null;
		try {
			// Setup the DataSource object

			con = DataConnect.getConnection();

			// Get a prepared SQL statement
			String sql = "SELECT user_name,user_id from users where user_name = ? and pwd = ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, password);
			// Execute the statement
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				FacesContext fc = FacesContext.getCurrentInstance();
				ExternalContext ec = fc.getExternalContext();
				String role=rs.getString("role");
				if(role.equals("user")) {
					ec.redirect("user_dash.xhtml");
				}
				else if(role.equals("admin")) {
					ec.redirect("admin_dash.xhtml");
				}
				String id = rs.getString(2);
				DataConnect.close();
				return id;
			} else {

				DataConnect.close();
				return "false";
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			if (con != null)
				DataConnect.close();
		}
		return "false";
	}

	public static String register(String user, String name, String email, String password) {
		Connection con = null;
		try {

			// Get a connection object
			con = DataConnect.getConnection();

			String sql_check = "select user_name from users where user_name=? ";

			// Get a prepared SQL statement
			String sql = "INSERT INTO USERS" + "(user_name, name, email, pwd) VALUES" + "(?,?,?,?)";

			PreparedStatement st2 = con.prepareStatement(sql_check);
			st2.setString(1, user);
			ResultSet rs = st2.executeQuery();
			if (rs.next()) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage("Username Already Exists!");
				facesContext.addMessage("main_form:uname", facesMessage);
				return "Already Exists";

			} else {
				PreparedStatement st = con.prepareStatement(sql);
				st.setString(1, user);
				st.setString(2, name);
				st.setString(3, email);
				st.setString(4, password);

				// Execute the statement
				if (st.executeUpdate() > 0) {
					con.close();
					return "true";
				}
			}

		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return "false";
	}

	public static String update(String uname, String name, String email, String pwd, Integer uid) {
		// TODO Auto-generated method stub
		Connection con = null;
		try {

			// Get a connection object
			con = DataConnect.getConnection();

			String sql_check = "select user_name from users where user_name=? ";
			PreparedStatement st2 = con.prepareStatement(sql_check);
			st2.setString(1, uname);
			ResultSet rs = st2.executeQuery();
			rs.next();
			if (rs.next()) {
				FacesContext facesContext = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage("Username Already Exists!");
				facesContext.addMessage("main_form:uname", facesMessage);
				return "Already Exists";

			}

			// Get a prepared SQL statement
			String sql = "UPDATE USERS SET " + "user_name=? ," + "name=? ," + "email=? ," + "pwd=? "
					+ "where user_id=?";

			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, uname);
			st.setString(2, name);
			st.setString(3, email);
			st.setString(4, pwd);
			st.setInt(5, uid);

			// Execute the statement
			if (st.executeUpdate() > 0) {
				con.close();
				return "true";
			}

		} catch (Exception e) {
			System.err.println("Exception: " + e.getMessage());
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
			}
		}
		return "false";
	}

	
	public static String getUid() {
		String uid = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userid");
		return uid;
	}

}
