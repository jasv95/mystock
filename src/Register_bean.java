import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

@ManagedBean(name = "Register_bean")
@ApplicationScoped
public class Register_bean {
	private String name, email, uname, pwd;
	private Integer uid;

	public Register_bean() {
		// TODO Auto-generated constructor stub
		System.out.println("in Register_bean constructor");
		String u_name=(String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().
				get("username");
		if(u_name!=null) {
			try {
				Connection con = DataConnect.getConnection();
				// Get a prepared SQL statement
				String sql = "SELECT * from users where user_name = "+"'"+u_name+"'";
				PreparedStatement st = con.prepareStatement(sql);
				// Execute the statement
				ResultSet rs= st.executeQuery();
				rs.next();
				uid = rs.getInt(1);
				uname = rs.getString(2);
				email = rs.getString(3);
				name = rs.getString(6);
				System.out.println(uname+email+name);
				DataConnect.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param fn
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the uname
	 */
	public String getUname() {
		return uname;
	}

	/**
	 * @param uname
	 *            the uname to set
	 */
	public void setUname(String uname) {
		this.uname = uname;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return pwd;
	}

	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String validate() {
		System.out.println("in validate");
		String result = LoginDAO.register(uname, name, email, pwd);
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		try {
			if(result.equals("true"))
			{
				FacesContext facesContext = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage("You are succesfully Registered");
				facesContext.addMessage("login_form:pwd", facesMessage);
				Flash fl= FacesContext.getCurrentInstance().getExternalContext().getFlash();
				fl.setKeepMessages(true);
				
				ec.redirect("login.xhtml");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String updateProfile() {
		System.out.println("in Register_Bean.updateProfile()");
		String result = LoginDAO.update(uname, name, email, pwd,uid);
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		try {
			if(result.equals("true"))
			{
				FacesContext facesContext = FacesContext.getCurrentInstance();
				FacesMessage facesMessage = new FacesMessage("You are succesfully Updated login again");
				facesContext.addMessage("login_form:pwd", facesMessage);
				Flash fl= FacesContext.getCurrentInstance().getExternalContext().getFlash();
				fl.setKeepMessages(true);
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("username", uname);
				ec.redirect("login.xhtml");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
}
