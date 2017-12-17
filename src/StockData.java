import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "StockData")
@ViewScoped
public class StockData {
	private static final long serialVersionUID = 1L;
	Connection con;
	Statement ps;
	ResultSet rs;
	private ArrayList<callAPI.StockDetails> WatchList;
	private List allStock;
	private String stockList;
	private String s_price,s_sym,s_qty,s_amt;
	private String table2Markup,table1Markup;
    private ArrayList<SelectItem> availableInterval;
    private String selectedInterval;
    private ArrayList<SelectItem> availableFunction;
    private String selectedFunction;
	
    
    
	public String getTable1Markup() {
		return table1Markup;
	}

	public void setTable1Markup(String table1Markup) {
		this.table1Markup = table1Markup;
	}

	public StockData() {
		availableFunction = new ArrayList<SelectItem>();
		availableInterval = new ArrayList<SelectItem>();
		availableFunction.add(new SelectItem("TIME_SERIES_INTRADAY", "INTRADAY"));
		availableFunction.add(new SelectItem("TIME_SERIES_DAILY", "Daily"));
		availableFunction.add(new SelectItem("TIME_SERIES_WEEKLY", "Weekly"));
		availableFunction.add(new SelectItem("TIME_SERIES_MONTHLY", "Monthaly"));
	}

	public String getS_price() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request.getParameter("price") != null) {
			s_price = (String) request.getParameter("price");
        }
		return s_price;
	}

	public void setS_price(String s_price) {
		this.s_price = s_price;
	}


	public List<SelectItem> getAvailableInterval() {
		return availableInterval;
	}

	public void setAvailableInterval(ArrayList<SelectItem> availableinterval) {
		this.availableInterval = availableinterval;
	}

	public String getSelectedInterval() {
		return selectedInterval;
	}

	public void setSelectedInterval(String selectedInterval) {
		this.selectedInterval = selectedInterval;
	}

	public List<SelectItem> getAvailableFunction() {
		return availableFunction;
	}

	public void setAvailableFunction(ArrayList<SelectItem> availableFunction) {
		this.availableFunction = availableFunction;
	}

	public String getSelectedFunction() {
		return selectedFunction;
	}

	public void setSelectedFunction(String selectedFunction) {
		this.selectedFunction = selectedFunction;
	}

	public void changeTypeState(AjaxBehaviorEvent event) {
		System.out.println("in changetype");
		if(selectedFunction.equals("TIME_SERIES_INTRADAY")) {
			availableInterval.add(new SelectItem("1min", "1min"));
			availableInterval.add(new SelectItem("5min", "5min"));
			availableInterval.add(new SelectItem("15min", "15min"));
			availableInterval.add(new SelectItem("30min", "30min"));
			availableInterval.add(new SelectItem("60min", "60min"));
		}
		else {
			availableInterval.clear();
		}
		/*else if(selectedFunction.equals("TIME_SERIES_DAILY")) {
			
		}
		else if(selectedFunction.equals("TIME_SERIES_WEEKLY")) {
			
		}
		else if(selectedFunction.equals("TIME_SERIES_MONTHLY")) {
			
		}
		*/
	}

	public String getTable2Markup() {
		return table2Markup;
	}

	public void setTable2Markup(String table2Markup) {
		this.table2Markup = table2Markup;
	}

	public String getS_sym() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request.getParameter("symbol") != null) {
            s_sym = (String) request.getParameter("symbol");
        }
		return s_sym;
	}

	public void setS_sym(String s_sym) {
		this.s_sym = s_sym;
	}

	public String getS_qty() {
		return s_qty;
	}

	public void setS_qty(String s_qty) {
		this.s_qty = s_qty;
	}

	public String getS_amt() {
		return s_amt;
	}

	public void setS_amt(String s_amt) {
		this.s_amt = s_amt;
	}

	public ArrayList<callAPI.StockDetails> getWatchList() {

		return WatchList;

	}

	public String getStockList() {
		return stockList;
	}

	@SuppressWarnings("rawtypes")
	public void setStockList(String stockList) {
		this.stockList = stockList;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getAllStock() {
		allStock = new ArrayList();
		int i = 0;
		try {
			con = DataConnect.getConnection();
			ps = con.createStatement();
			rs = ps.executeQuery("select * from stockdetails");
			while (rs.next()) {
				//1 is id 2 is stock name
				allStock.add(i, new StockDetail(rs.getString(1), rs.getString(2)));
				i++;
			}
		}
		catch (Exception e) {
			System.out.println("Error Data : " + e.getMessage());
		}

		return allStock;
	}

	public class StockDetail {
		public String stockId;
		public String stockName;

		public StockDetail(String stockId, String stockName) {
			this.stockId = stockId;
			this.stockName = stockName;
		}

		public String getStockId() {
			return stockId;
		}

		public String getStockName() {
			return stockName;
		}
	}
	
	public String viewStock() {
		System.out.println(stockList);
		table2Markup = callAPI.getAllDetails(stockList,selectedFunction,selectedInterval);
		table1Markup = callAPI.gettable1Markup();
		return null;
	}
	
	public String addToWatchlist() {
		StockDAO.addToWatchlist(stockList);
		return null;
	}
	
	public String viewWatchlist() {
		List<String> stocks= new ArrayList<String>();
		stocks=StockDAO.getWatchlist();
		callAPI.StockDetails sd=null;
		WatchList= new ArrayList<callAPI.StockDetails>();
		for(int i=0;i<stocks.size();i++) {
			sd = callAPI.getStockDetails(stocks.get(i),"1min");
			WatchList.add(sd);
			//makeMarkypTable(stocks.get(i), "1min");
		}
		return "watchlist.xhtml";
	}
	
	
	public String buyStock(String sym,String price) {


		System.out.println("inside Stockdata.buyStock");
		s_amt="0";
		s_price=price;
		s_sym = sym;
		s_qty="0";
		
		System.out.println(sym);
		
		return "trading.xhtml";
		
	}
	
	public String buy() {

		System.out.println(s_qty);
		StockDAO.buy(s_sym,s_price,s_qty);
		return "Account.xhtml";
	}
	
	public String sell() {

		System.out.println(s_qty);
		StockDAO.sell(s_sym,s_price,s_qty);
		return "Account.xhtml";
	}
	
}