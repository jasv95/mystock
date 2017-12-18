import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "ManagerBean")
@RequestScoped
public class ManagerBean {
	private ArrayList<ManagerDAO.MsgDetail> md;

	public ManagerBean() {
		super();
		md = ManagerDAO.getMsgList();
		
	}

	public ArrayList<ManagerDAO.MsgDetail> getMd() {
		return md;
	}

	public void setMd(ArrayList<ManagerDAO.MsgDetail> md) {
		this.md = md;
	}
}
