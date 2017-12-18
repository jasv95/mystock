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
			String sql = "SELECT user_name,user_id,role from users where user_name = ? and pwd = ? and status=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, user);
			st.setString(2, password);
			st.setInt(3, 1);
			// Execute the statement
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				FacesContext fc = FacesContext.getCurrentInstance();
				ExternalContext ec = fc.getExternalContext();
				String role = rs.getString("role");
				if (role.equals("user")) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", "user");
					ec.redirect("user_dash.xhtml");
				} else if (role.equals("admin")) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", "admin");
					ec.redirect("admin_dash.xhtml");
				}
				else if(role.equals("manager")) {
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("role", "admin");
					ec.redirect("manager_dash.xhtml");
				}
				String id = rs.getString("user_id");
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

	public static String register(String user, String name, String email, String password, String selectedRole,
			String fee) {
		Connection con = null;
		try {

			// Get a connection object
			con = DataConnect.getConnection();

			String sql_check = "select user_name from users where user_name=? ";
			String sql = null;
			// Get a prepared SQL statement
			if (selectedRole.equals("user")) {
				sql = "INSERT INTO USERS" + "(user_name, name, email, pwd, role,status) VALUES" + "(?,?,?,?,?,1)";
			} else {
				sql = "INSERT INTO USERS" + "(user_name, name, email, pwd, role,status) VALUES" + "(?,?,?,?,?,0)";
			}
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
				st.setString(5, selectedRole);

				// Execute the statement
				if (st.executeUpdate() > 0) {
					if (selectedRole.equals("manager")) {
						sql = "select user_id from users where user_name =? and pwd = ?";
						st2.close();
						st2 = con.prepareStatement(sql);
						st2.setString(1, user);
						st2.setString(2, password);
						rs = st2.executeQuery();
						rs.next();
						int uid = rs.getInt("user_id");
						sql = "insert into manager_detail (user_id,fees) values(?,?)";
						st2.close();
						st2 = con.prepareStatement(sql);
						st2.setInt(1, uid);
						st2.setDouble(2, Double.valueOf(fee));
						if(st2.executeUpdate()>0) {
							con.close();
							return "true";
						}
						else {
							con.close();
							return "false";
						}
					}
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

	public static boolean selectManager(int mid) {
		// TODO Auto-generated method stub
		try {
			Connection con = DataConnect.getConnection();
			String sql = "update users set mid = ? where user_id=?";
			int uid = Integer.parseInt(getUid());
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, mid);
			st.setInt(2, uid);
			if(st.executeUpdate()>0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}

	public static void createMsg(double reqAmt, int m_id,String msg) {
		// TODO Auto-generated method stub
		try {
			Connection con = DataConnect.getConnection();
			String sql = "Insert into messages (user_id,manager_id,msg,amount)"
					+ "values(?,?,?,?)";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(getUid()));
			st.setInt(2, m_id);
			st.setString(3, msg);
			st.setDouble(4, reqAmt);
			st.executeUpdate();
			double balance = AccountDAO.getAccBalance(Integer.parseInt(getUid()));
			if(reqAmt<=balance) {
				double NewBalance = balance-reqAmt;
				AccountDAO.setAccBalance(Integer.parseInt(getUid()), NewBalance);
				double m_balance=AccountDAO.getAccBalance(m_id);
				m_balance += reqAmt;
				AccountDAO.setAccBalance(m_id, m_balance);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
