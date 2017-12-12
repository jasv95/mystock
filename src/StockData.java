import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "stockData", eager = true)
@SessionScoped
public class StockData {
	private static final long serialVersionUID = 1L;
	Connection con;
	Statement ps;
	ResultSet rs;
	private List stockList = new ArrayList();

	public List getSymptomList() {
		return stockList;
	}

	public void setSymptomList(List stockList) {
		this.stockList = stockList;
	}

	public List getAllSym() {
		int i = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DataConnect.getConnection();
			ps = con.createStatement();
			rs = ps.executeQuery("select * from stockdetails");
			while (rs.next()) {
				stockList.add(i, new StockDetail(rs.getString(1), rs.getString(2)));
				i++;
			}
		}

		catch (Exception e) {
			System.out.println("Error Data : " + e.getMessage());
		}

		return stockList;
	}

	public class StockDetail {
		public String stockId;
		public String stockName;

		public StockDetail(String stockId, String stockName) {
			this.stockId = stockId;
			this.stockName = stockName;
		}

		public String getSymptomId() {
			return stockId;
		}

		public String getSymptomName() {
			return stockName;
		}
	}
}