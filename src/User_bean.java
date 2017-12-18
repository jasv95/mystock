

import java.io.IOException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "User_bean")
@SessionScoped
public class User_bean {
	private String user, pwd,msg;
	private ArrayList<ManagerDAO.ManagerDetails> managerList,myManager;
	private String reqAmt;
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getReqAmt() {
		return reqAmt;
	}

	public void setReqAmt(String reqAmt) {
		this.reqAmt = reqAmt;
	}

	public ArrayList<ManagerDAO.ManagerDetails> getMyManager() {

		getmyManagerDetails();
		return myManager;
	}

	public void setMyManager(ArrayList<ManagerDAO.ManagerDetails> myManager) {
		this.myManager = myManager;
	}

	public User_bean() {
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	

	public ArrayList<ManagerDAO.ManagerDetails> getManagerList() {
		return managerList;
	}

	public void setManagerList(ArrayList<ManagerDAO.ManagerDetails> managerList) {
		this.managerList = managerList;
	}

	public String validate() {
		String valid = LoginDAO.login(user, pwd);
		if (!valid.equals("false")) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", user);
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userid", valid);
			return "user_dash.xhtml";
			
        } else {
        	FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage facesMessage = new FacesMessage("Invalid username or password");
			facesContext.addMessage("login_form:pwd", facesMessage);
        }
		return null;
	}
	
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		try {
			ec.redirect("login.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String show() {
		
		return null;
	}
	
	public String redToAccount() {
		return "Account.xhtml";
	}
	
	public String viewManager() {
		managerList= new ArrayList<ManagerDAO.ManagerDetails>();
		managerList = ManagerDAO.getManagerInfo();
		System.out.println(String.valueOf(managerList.size()));
		return "manager_display.xhtml";
	}
	
	public String selectManager(int mid) {
		LoginDAO.selectManager(mid);
		return "Account.xhtml";
	}
	
	public String getmyManagerDetails() {
		myManager=ManagerDAO.getMyManager();
		return null;
	}
	
	public String redToUserDashboard() {
		return "user_dash.xhtml";
	}
	
	public String ContactManager() {
		return "Contact.xhtml";
	}
	
	public String sendMsg() {
		LoginDAO.createMsg(Double.parseDouble(reqAmt),myManager.get(0).getM_id(),msg);
		return "user_dash.xhtml";
	}
}
