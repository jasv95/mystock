import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class callAPI {
	private static ArrayList<StockDetails> resultList;
	private static String API_KEY="Z6C2QVO9DWOBUKJ5";
	private static String table1Markup,table2Markup;
	private static String selectedFunction;
	public static void installAllTrustingManager() {
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
	
	public static StockDetails getStockDetails(String symbol,String interval) {
		System.out.println("inside callAPI.getStockDetails");
		StockDetails sd = null;
		installAllTrustingManager();
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + interval + "&outputsize=compact&apikey=" + API_KEY;
        System.out.println(url);
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
                JsonObject jsob = (JsonObject) mainJsonObj.get(key);
                
                
            } else {
                JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
         
                String subKey=dataJsonObj.keySet().iterator().next();
                System.out.println("subkey is "+subKey);
                JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                sd = new StockDetails("", symbol,subJsonObj.getString("4. close"),subJsonObj.getString("5. volume"));
                }
            }
		return sd;
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

public static String getAllDetails(String st_symbol, String selectedFunction, String selectedInterval) {
	// TODO Auto-generated method stub
	installAllTrustingManager();
	
    String url=null;
	

    //System.out.println("selectedItem: " + this.selectedSymbol);
    //System.out.println("selectedInterval: " + this.selectedInterval);
    String symbol = st_symbol;
    if(selectedInterval.equals("")) {
    	selectedInterval = "1min";
    }
    if(selectedFunction.equals("")) {
    	selectedFunction="TIME_SERIES_INTRADAY";
    }
    System.out.println("Selected fun"+selectedFunction);
    if(selectedFunction.equals("TIME_SERIES_INTRADAY")|| selectedFunction.equals("")) {
    	url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + selectedInterval +"&outputsize=compact&apikey=" + API_KEY;
    }
    else {
    	url = "https://www.alphavantage.co/query?function="+selectedFunction+"&symbol=" + symbol +"&outputsize=compact&apikey=" + API_KEY;
    }
    System.out.println(url);
    table1Markup += "URL::: <a href='" + url + "'>Data Link</a><br>";
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
    for (String key : mainJsonObj.keySet()) {
        if (key.equals("Meta Data")) {
            table1Markup = null; // reset table 1 markup before repopulating
            JsonObject jsob = (JsonObject) mainJsonObj.get(key);
            table1Markup += "<style>#detail >tbody > tr > td{ text-align:center;}</style><b>Stock Details</b>:<br>";
            table1Markup += "<table>";
            table1Markup += "<tr><td>Information</td><td>" + jsob.getString("1. Information") + "</td></tr>";
            table1Markup += "<tr><td>Symbol</td><td>" + jsob.getString("2. Symbol") + "</td></tr>";
            table1Markup += "<tr><td>Last Refreshed</td><td>" + jsob.getString("3. Last Refreshed") + "</td></tr>";
            if(selectedFunction.equals("TIME_SERIES_INTRADAY")) {
            table1Markup += "<tr><td>Interval</td><td>" + jsob.getString("4. Interval") + "</td></tr>";
            table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("5. Output Size") + "</td></tr>";
            table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("6. Time Zone") + "</td></tr>";
            }
            else {
                table1Markup += "<tr><td>Output Size</td><td>" + jsob.getString("4. Output Size") + "</td></tr>";
                table1Markup += "<tr><td>Time Zone</td><td>" + jsob.getString("5. Time Zone") + "</td></tr>";          	
            }
            table1Markup += "</table>";
        } else {
            table2Markup = null; // reset table 2 markup before repopulating
            JsonObject dataJsonObj = mainJsonObj.getJsonObject(key);
            table2Markup += "<table class='table table-hover'>";
            table2Markup += "<thead><tr><th>Timestamp</th><th>Open</th><th>High</th><th>Low</th><th>Close</th><th>Volume</th></tr></thead>";
            table2Markup += "<tbody>";
            int i = 0;
            for (String subKey : dataJsonObj.keySet()) {
                JsonObject subJsonObj = dataJsonObj.getJsonObject(subKey);
                table2Markup
                        += "<tr>"
                        + "<td>" + subKey + "</td>"
                        + "<td>" + subJsonObj.getString("1. open") + "</td>"
                        + "<td>" + subJsonObj.getString("2. high") + "</td>"
                        + "<td>" + subJsonObj.getString("3. low") + "</td>"
                        + "<td>" + subJsonObj.getString("4. close") + "</td>"
                        + "<td>" + subJsonObj.getString("5. volume") + "</td>";
                if (i == 0) {
                    String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                //    table2Markup += "<td><a class='btn btn-success' href='" + path + "/faces/trading.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy Stock</a></td>";
                    	table2Markup += "<td><a class='btn btn-success' href='" + path + "/faces/trading.xhtml?symbol=" + symbol + "&price=" + subJsonObj.getString("4. close") + "'>Buy/Sell Stock</a></td>";
                    
                }
                table2Markup += "</tr>";
                i++;
            }
            table2Markup += "</tbody></table>";
        }
    }
    return table2Markup;
}

public static String gettable1Markup() {
	// TODO Auto-generated method stub
	return table1Markup;
}
	
}
