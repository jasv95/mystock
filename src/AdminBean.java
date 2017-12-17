import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

@ManagedBean(name = "AdminBean")
@RequestScoped
public class AdminBean {

	private ArrayList<AccountBean.UserActivity> activityList;
	private ArrayList<AdminDAO.ManagerDetails> managerList;
	private List<SelectItem> userList;
	private String selecteduser;
	
	
	
	public AdminBean() {
		super();
		createManagerList();
	}
	
	public ArrayList<AccountBean.UserActivity> getActivityList() {
		return activityList;
	}
	public void setActivityList(ArrayList<AccountBean.UserActivity> activityList) {
		this.activityList = activityList;
	}
	public ArrayList<AdminDAO.ManagerDetails> getManagerList() {
		return managerList;
	}
	public void setManagerList(ArrayList<AdminDAO.ManagerDetails> managerList) {
		this.managerList = managerList;
	}
	public List<SelectItem> getuserList() {
		userList = new ArrayList<SelectItem>();
		try {
			Connection con = DataConnect.getConnection();
			Statement ps = con.createStatement();
			ResultSet rs = ps.executeQuery("select user_name,user_id from users");
			int i=0;
			while (rs.next()) {
				//1 is id 2 is stock name
				userList.add(new SelectItem(rs.getString("user_id"), rs.getString("user_name")));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userList;
	}
	public void setUserList(List<SelectItem> userList) {
		this.userList = userList;
	}
	public String getSelecteduser() {
		return selecteduser;
	}
	public void setSelecteduser(String selecteduser) {
		this.selecteduser = selecteduser;
	}
	
	public String viewActivity() {
		activityList = new ArrayList<AccountBean.UserActivity>();
		String user_id = selecteduser;
		ResultSet rs = AccountDAO.getActivities(Integer.parseInt(user_id));
		try {
			while(rs.next()) {
				activityList.add(
				new AccountBean.UserActivity(
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
		return null;
	}
	
	public String createManagerList() {
		managerList = AdminDAO.getManagerList();
		return null;
	}
	
	public String approveManager(int manager_id) {
		if( AdminDAO.approveManager(manager_id)) {
			System.out.println("true");
		}
		else {
			System.out.println("false");
		}
		return null;
	}
}
