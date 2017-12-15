

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "User_bean")
@ApplicationScoped
public class User_bean {
	private String user, pwd;

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
}
