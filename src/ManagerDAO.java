import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ManagerDAO {

	public static ArrayList<ManagerDetails> getManagerInfo(){
		ArrayList<ManagerDetails> ml = new ArrayList<ManagerDetails>();
		try {
			Connection con = DataConnect.getConnection();
			String sql = "select user_id,name,email,fees from users natural join manager_detail where role='manager'";
		    PreparedStatement st = con.prepareStatement(sql);
		    ResultSet rs = st.executeQuery();
		    while(rs.next()) {
		    	ml.add(new ManagerDetails
		    			(rs.getInt("user_id"),rs.getString("name"),rs.getString("email"),rs.getDouble("fees")));
		    	System.out.println(rs.getString("name"));
		    }
			return ml;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static class ManagerDetails{
		private String name,email;
		private int m_id;
		private double fee;
		
		public ManagerDetails(int m_id, String name, String email, double fee) {
			super();
			this.m_id = m_id;
			this.name = name;
			this.email = email;
			this.fee = fee;
		}
		
		public int getM_id() {
			return m_id;
		}
		public void setM_id(int m_id) {
			this.m_id = m_id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public double getFee() {
			return fee;
		}
		public void setFee(double fee) {
			this.fee = fee;
		}
		
	}

	public static ArrayList<ManagerDetails> getMyManager() {
		ArrayList<ManagerDetails> ml = new ArrayList<ManagerDetails>();
		try {
			Connection con = DataConnect.getConnection();
			String sql = "select mid from users where user_id=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1,Integer.parseInt(LoginDAO.getUid()));
			ResultSet rs = st.executeQuery();
			rs.next();
			int mid=rs.getInt("mid");
			sql = "select user_name,name,email,fees from users natural join manager_detail where user_id = ?";
			st.close();
			st = con.prepareStatement(sql);
			st.setInt(1,mid);
			rs = st.executeQuery();
			if(rs.next()) {
				ml.add(new ManagerDetails(mid, rs.getString("name"), rs.getString("email"), rs.getDouble("fees")));
			}
			return ml;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	public static class MsgDetail{

		private String uname,msg,time;
		private double amt;
		
		public MsgDetail(String uname, String msg, String time, double amt) {
			super();
			this.uname = uname;
			this.msg = msg;
			this.time = time;
			this.amt = amt;
		}
		public String getUname() {
			return uname;
		}
		public void setUname(String uname) {
			this.uname = uname;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public double getAmt() {
			return amt;
		}
		public void setAmt(double amt) {
			this.amt = amt;
		}
	}

	public static ArrayList<MsgDetail> getMsgList(){

		ArrayList<MsgDetail> md = new ArrayList<MsgDetail>();
		try {
			Connection con = DataConnect.getConnection();
			String sql = "Select * from messages,users where messages.user_id=users.user_id and messages.manager_id=? and messages.status is NULL";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(LoginDAO.getUid()));
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				md.add(new MsgDetail(rs.getString("user_name"), rs.getString("msg"), rs.getString("time"), rs.getDouble("amount")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md;
	}
}
