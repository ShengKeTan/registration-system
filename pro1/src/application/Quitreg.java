package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Quitreg {

	private final StringProperty regnum;
	private final StringProperty depname;
	private final StringProperty docname;
	private final StringProperty time;
	private final StringProperty type;
	private final StringProperty quitreg;
	private final StringProperty regfee;
	
	public Quitreg(String regnum, String depname, String docname, String time, String type, String quitreg, String regfee) {
		this.depname = new SimpleStringProperty(depname);
		this.regnum = new SimpleStringProperty(regnum);
		this.docname = new SimpleStringProperty(docname);
		this.type = new SimpleStringProperty(type);
		this.time = new SimpleStringProperty(time);
		this.quitreg = new SimpleStringProperty(quitreg);
		this.regfee = new SimpleStringProperty(regfee);
	}
	
	public String getRegnum() {
		return regnum.get();
	}
	public String getDepname() {
		return depname.get();
	}
	public String getDocname() {
		return docname.get();
	}
	public String getTime() {
		return time.get();
	}
	public String getType() {
		return type.get();
	}
	public String getQuitreg() {
		return quitreg.get();
	}
	public String getRegfee() {
		return regfee.get();
	}
}
