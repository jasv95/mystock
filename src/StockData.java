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
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@ManagedBean(name = "StockData")
@ApplicationScoped
public class StockData {
	private static final long serialVersionUID = 1L;
	Connection con;
	Statement ps;
	ResultSet rs;
	private ArrayList<StockDetails> WatchList;
	private List allStock;
	private String stockList;
	private String API_KEY="Z6C2QVO9DWOBUKJ5";
	private String table1Markup,table2Markup;
	private String s_price,s_sym,s_qty,s_amt;
	
	public String getS_price() {
		return s_price;
	}

	public void setS_price(String s_price) {
		this.s_price = s_price;
	}

	public String getS_sym() {
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

	public ArrayList<StockDetails> getWatchList() {

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
	
	public String show() {
		System.out.println(stockList);
		return null;
	}
	
	public String addToWatchlist() {
		StockDAO.addToWatchlist(stockList);
		return null;
	}
	
	public void viewWatchlist() {
		List<String> stocks= new ArrayList<String>();
		stocks=StockDAO.getWatchlist();
		WatchList= new ArrayList<StockDetails>();
		for(int i=0;i<stocks.size();i++) {
			makeMarkypTable(stocks.get(i), "1min");
		}
		return ;
	}
	
	public void installAllTrustingManager() {
        TrustManager[] trustAllCerts;
        trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            System.out.println("Exception :" + e);
        }
        return;
    }
	
	public void makeMarkypTable(String symbol,String interval) {
		System.out.println("inside StockData.makeTable");
		installAllTrustingManager();
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&apikey=" + API_KEY;
        System.out.println(url);
        this.table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
        InputStream inputStream = null;
		try {
			inputStream = new URL(url).openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // convert the json string back to object
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonObject mainJsonObj = jsonReader.readObject();
        String Timezone;
        System.out.println(mainJsonObj.toString());
		for (String key : mainJsonObj.keySet()) {
            if (key.equals("Meta Data")) {
                this.table1Markup = null; // reset table 1 markup before repopulating
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                this.table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
                this.table1Markup += "<table>";
                this.table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
                this.table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
                this.table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
                this.table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
                this.table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
                this.table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
                this.table1Markup += "</table>";
                
                
            } else {
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
         
                String subKey=dataJsonObj.keySet().iterator().next();
                System.out.println("subkey is "+subKey);
                JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                StockDetails sd = new StockDetails("", symbol,subJsonObj.getString("4. close"),subJsonObj.getString("5. volume"));
                WatchList.add(sd);  
                }
            }
		System.out.println(table2Markup);
		return;
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
	
	public static class StockDetails{
		
		private String name,symbol,price,volume;
		
		public StockDetails(String name, String symbol,String price,String volume) {
			this.name=name;
			this.symbol=symbol;
			this.price=price;
			this.volume=volume;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getVolume() {
			return volume;
		}

		public void setVolume(String volume) {
			this.volume = volume;
		}
	}
	
	public String buy() {

		System.out.println(s_qty);
		StockDAO.buy(s_sym,s_price,s_qty);
		return null;
	}
	
	public String sell() {

		System.out.println(s_qty);
		StockDAO.sell(s_sym,s_price,s_qty);
		return null;
	}
	
}