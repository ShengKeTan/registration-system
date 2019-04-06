package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Income {
	
	private final StringProperty depname;
	private final StringProperty docid;
	private final StringProperty docname;
	private final StringProperty type;
	private final StringProperty number;
	private final StringProperty incom;
	
	public Income(String depname, String docid, String docname, String type, String number, String incom) {
		this.depname = new SimpleStringProperty(depname);
		this.docid = new SimpleStringProperty(docid);
		this.docname = new SimpleStringProperty(docname);
		this.type = new SimpleStringProperty(type);
		this.number = new SimpleStringProperty(number);
		this.incom = new SimpleStringProperty(incom);
	}
	
	public String getDepname() {
		return depname.get();
	}
	public String getDocid() {
		return docid.get();
	}
	public String getDocname() {
		return docname.get();
	}
	public String getType() {
		return type.get();
	}
	public String getNumber() {
		return number.get();
	}
	public String getIncom() {
		return incom.get();
	}

}
