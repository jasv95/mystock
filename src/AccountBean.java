import java.awt.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "AccountBean")
@RequestScoped
public class AccountBean {

	private String user_name,user_id,balance;
	private ArrayList<UserActivity> activityList;
	private ArrayList<CurrentHoldings> holdingList;
	
	public ArrayList<CurrentHoldings> getHoldingList() {
		return holdingList;
	}


	public void setHoldingList(ArrayList<CurrentHoldings> holdingList) {
		this.holdingList = holdingList;
	}


	public AccountBean() {
		createActivity();
		createHoldingList();
	}
	

	public String getUser_name() {
		return user_name;
	}

	public ArrayList<UserActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<UserActivity> activityList) {
		this.activityList = activityList;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String viewAccount() {
		createActivity();
		return null;
	}
	
	public void createActivity() {
		activityList = new ArrayList<UserActivity>();
		String user_id = LoginDAO.getUid();
		ResultSet rs = AccountDAO.getActivities(Integer.parseInt(user_id));
		try {
			while(rs.next()) {
				activityList.add(
				new UserActivity(
						rs.getString("st_symbol"),
						rs.getString("date"),
						rs.getString("trade_type"),
						rs.getInt("st_qty"),
						rs.getDouble("unit_price")
								)
						);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createHoldingList() {
		holdingList = new ArrayList<CurrentHoldings>();
		String user_id = LoginDAO.getUid();
		ResultSet rs = AccountDAO.getHoldings(Integer.parseInt(user_id));
		try {
			while(rs.next()) {
				holdingList.add(
				new CurrentHoldings(
						rs.getString("st_symbol"),
						rs.getInt("st_qty")
								)
						);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class CurrentHoldings{



		private String st_sym;
		private int st_qty;

		
		
		public CurrentHoldings(String st_sym, int st_qty) {
			super();
			this.st_sym = st_sym;
			this.st_qty = st_qty;
		}

		public String getSt_sym() {
			return st_sym;
		}

		public void setSt_sym(String st_sym) {
			this.st_sym = st_sym;
		}

		public int getSt_qty() {
			return st_qty;
		}

		public void setSt_qty(int st_qty) {
			this.st_qty = st_qty;
		}

	}
	
	public static class UserActivity{


		private String st_symbol,date,trade_type;
		private int st_qty;
		private double unit_price;
		private double t_price;
		
		public UserActivity(String st_symbol, String date, String trade_type, int st_qty, double unit_price) {

			super();
			this.st_symbol = st_symbol;
			this.date = date;
			this.trade_type = trade_type;
			this.st_qty = st_qty;
			this.unit_price = unit_price;
			this.t_price = t_price;
		}
		
		public String getSt_symbol() {
			return st_symbol;
		}
		public void setSt_symbol(String st_symbol) {
			this.st_symbol = st_symbol;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getTrade_type() {
			return trade_type;
		}
		public void setTrade_type(String trade_type) {
			this.trade_type = trade_type;
		}
		public int getSt_qty() {
			return st_qty;
		}
		public void setSt_qty(int st_qty) {
			this.st_qty = st_qty;
		}
		public double getUnit_price() {
			return unit_price;
		}
		public void setUnit_price(double unit_price) {
			this.unit_price = unit_price;
		}
		public double getT_price() {
			t_price = st_qty*unit_price;
			return t_price;
		}
		public void setT_price(double t_price) {
			this.t_price = t_price;
		}
	}
}
