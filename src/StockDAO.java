import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class StockDAO {

	public static String getStockDetails(String id) {
		String json = null;

		return json;
	}

	public static String addToWatchlist(String stockSymbol) {

		// TODO Auto-generated method stub
		System.out.println("inside StockDAO.addToWatchList");
		try {
			Connection con = DataConnect.getConnection();
			String sql_check = "select user_id from watchlist where user_id=? and stock_id=? ";
			String uid = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userid");

			PreparedStatement st2 = con.prepareStatement(sql_check);
			st2.setInt(1, Integer.parseInt(uid));
			st2.setString(2, stockSymbol);
			ResultSet rs = st2.executeQuery();
			if (rs.next()) {
				DataConnect.close();
				return "Already Exists";

			} else {
				// Get a prepared SQL statement
				String sql = "INSERT INTO watchlist" + "(user_id,stock_id) VALUES" + "(?,?)";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, Integer.parseInt(uid));
				st.setString(2, stockSymbol);

				// Execute the statement
				if (st.executeUpdate() > 0) {
					DataConnect.close();
					return "true";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getWatchlist() {

		// TODO Auto-generated method stub
		List<String> stocks = new ArrayList<String>();
		try {
			Connection con = DataConnect.getConnection();
			String uid = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userid");
			String sql = "Select stock_id from watchlist where user_id= ?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(uid));
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				stocks.add(rs.getString(1));
			}
			DataConnect.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stocks;
	}

	public static String buy(String s_sym, String s_price, String s_qty) {
		System.out.println("inside StockDAO.buy");
		int avail_qty = 0;
		Double Total_price = Double.parseDouble(s_qty) * Double.parseDouble(s_price);
		String uid = LoginDAO.getUid();
		Double available_bal = LoginDAO.getAccBalance(Integer.parseInt(uid));
		if (available_bal < Total_price) {
			return "low_balance";
		} else {
			try {
				Connection con = DataConnect.getConnection();
				String sql_check = "Select st_qty from holdings where user_id=? and st_symbol =?";
				String sql = "INSERT INTO holdings" + "(user_id,st_symbol,st_qty) VALUES" + "(?,?,?)";
				PreparedStatement st = con.prepareStatement(sql_check);
				st.setInt(1, Integer.parseInt(uid));
				st.setString(2, s_sym);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					avail_qty = rs.getInt("st_qty");
					String sql2 = "Update holdings set st_qty = ?";
					st.close();
					st = con.prepareStatement(sql2);
					st.setInt(1, Integer.parseInt(s_qty) + avail_qty);
					// Execute the statement
					if (st.executeUpdate() > 0) {
						LoginDAO.setAccBalance(Integer.parseInt(uid), available_bal - Total_price);
						create_activirtRecord(Integer.parseInt(uid), s_sym, Double.parseDouble(s_price), s_qty, "buy");
						DataConnect.close();
						return "true";
					} else {
						return "error";
					}

				} else {
					st.close();
					st = con.prepareStatement(sql);
					st.setInt(1, Integer.parseInt(uid));
					st.setString(2, s_sym);
					st.setInt(3, Integer.parseInt(s_qty));

					// Execute the statement
					if (st.executeUpdate() > 0) {
						LoginDAO.setAccBalance(Integer.parseInt(uid), available_bal - Total_price);
						create_activirtRecord(Integer.parseInt(uid), s_sym, Double.parseDouble(s_price), s_qty, "buy");
						DataConnect.close();
						return "true";
					} else {
						return "error";
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	public static String sell(String s_sym, String s_price, String s_qty) {
		String uid = LoginDAO.getUid();
		System.out.println("inside StockDAO.sell");
		int avail_qty = 0;
		Double Total_price = Double.parseDouble(s_qty) * Double.parseDouble(s_price);
		Double available_bal = LoginDAO.getAccBalance(Integer.parseInt(uid));

		try {
			Connection con = DataConnect.getConnection();
			String sql_check = "Select st_qty from holdings where user_id=? and st_symbol =?";
			String sql = "update holdings set st_qty= ?";
			PreparedStatement st = con.prepareStatement(sql_check);
			st.setInt(1, Integer.parseInt(uid));
			st.setString(2, s_sym);
			System.out.println("inside StockDAO.sell");
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				avail_qty = rs.getInt("st_qty");
				if (avail_qty > Integer.parseInt(s_qty)) {
					st.close();
					st = con.prepareStatement(sql);
					st.setInt(1, avail_qty - Integer.parseInt(s_qty));
					// Execute the statement
					if (st.executeUpdate() > 0) {
						LoginDAO.setAccBalance(Integer.parseInt(uid), available_bal + Total_price);
						create_activirtRecord(Integer.parseInt(uid), s_sym, Double.parseDouble(s_price), s_qty, "sell");
						DataConnect.close();
						return "true";
					} else {
						return "error";
					}
				}
				else if(avail_qty==Integer.parseInt(s_qty)) {
					sql = "delete from holdings where user_id=? and st_symbol=?";
					st.close();
					st = con.prepareStatement(sql);
					st.setInt(1, Integer.parseInt(uid));
					st.setString(2, s_sym);
					// Execute the statement
					if (st.executeUpdate() > 0) {
						LoginDAO.setAccBalance(Integer.parseInt(uid), available_bal + Total_price);
						create_activirtRecord(Integer.parseInt(uid), s_sym, Double.parseDouble(s_price), s_qty, "sell");
						DataConnect.close();
						return "true";
					} else {
						return "error";
					}
				}
				else {
					return "less_holding";
				}
			} else {
				return "no_holding";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public static boolean create_activirtRecord(int user_id, String s_sym, Double s_price, String s_qty,
			String trade_type) {
		try {
			Connection con = DataConnect.getConnection();
			String sql = "INSERT INTO acc_activity" + "(user_id,st_symbol,st_qty,unit_price,trade_type) VALUES"
					+ "(?,?,?,?,?)";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_id);
			st.setString(2, s_sym);
			st.setInt(3, Integer.parseInt(s_qty));
			st.setDouble(4, s_price);
			st.setString(5, trade_type);

			// Execute the statement
			if (st.executeUpdate() > 0) {
				DataConnect.close();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
