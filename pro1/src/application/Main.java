package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;



public class Main extends Application {
	
	//舞台
	static Stage primarystage = null;
	//界面根结点
	private Parent chose_root = null;
	private Parent login_root = null;
	private Parent doc_root = null;
	private Parent pat_root = null;
	private Parent reg_root = null;
	//界面窗口
	private static Scene chose_scene = null;
	private static Scene logon_scene = null;
	private static Scene doc_scene = null;
	private static Scene pat_scene = null;
	private static Scene reg_scene = null;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//set root
			chose_root = FXMLLoader.load(getClass().getClassLoader().getResource("chose.fxml"));
			login_root = FXMLLoader.load(getClass().getClassLoader().getResource("logon.fxml"));
			doc_root = FXMLLoader.load(getClass().getClassLoader().getResource("doctorwin.fxml"));
			pat_root = FXMLLoader.load(getClass().getClassLoader().getResource("patwin.fxml"));
			reg_root =  FXMLLoader.load(getClass().getClassLoader().getResource("regnumwin.fxml"));
			//set scene
			chose_scene = new Scene(chose_root);
			logon_scene = new Scene(login_root);
			doc_scene = new Scene(doc_root);
			pat_scene = new Scene(pat_root);
			reg_scene = new Scene(reg_root);
			//set stage
			primarystage = primaryStage;
			setChoseUI(); //first scene chose
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public static void setPrimaryStage(Stage stage) {
		primarystage = stage;
	}
	public static void setChoseUI() {
		primarystage.setTitle("身份认证");
		primarystage.setScene(chose_scene);
	}
	public static void  setLoginUI() {
		primarystage.setTitle("登陆");
		primarystage.setScene(logon_scene);
	}
	public static void  setPatUI() {
		primarystage.setTitle("挂号窗口");
		primarystage.setScene(pat_scene);
	}
	public static void setDoctorUI() {
		primarystage.setTitle("医生操作台");
		primarystage.setScene(doc_scene);
	}
	public static void setRegnumUI() {
		primarystage.setTitle("挂号信息");
		primarystage.setScene(reg_scene);
	}
	public static Stage getPrimaryStage() {
		return primarystage;
	}
}
