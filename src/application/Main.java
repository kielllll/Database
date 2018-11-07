package application;
	
import com.sun.javafx.application.LauncherImpl;

import application.database.Database;
import application.util.Dialogs;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage stgLogin;
	private static Stage stgMenu;
	private static Stage stgUser;
	private static final int COUNT_LIMIT = 35000;
	private static String payrollID = "payroll";
	private static String payrollFile = "payroll/PayrollView.fxml";
	private static String employeesID = "employeesID";
	private static String employeesFile = "employees/EmployeesView.fxml";
	private static String companiesID = "companiesID";
	private static String companiesFile = "companies/CompaniesView.fxml";
	private static String unitsID = "unitsID";
	private static String unitsFile = "units/UnitsView.fxml";
	private static String salaryID = "salaryID";
	private static String salaryFile = "salary/SalaryView.fxml";
	private static String deductionID = "deductionID";
	private static String deductionFile = "deduction/DeductionView.fxml";
	 
	@Override
    public void init() throws Exception {
        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
		Database.getInstance().DBSetConnection();
        for (int i = 0; i < COUNT_LIMIT; i++) {
            double progress = (100 * i) / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
        }
    }
	
	@Override
	public void start(Stage primaryStage) {
		try {
			showLogin();
		} catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public static void showLogin() {
		try {
			stgLogin = new Stage();
			stgLogin.setResizable(false);
			stgLogin.setTitle("Three Seven Payroll System");
			stgLogin.getIcons().add(new Image(Main.class.getResourceAsStream("books.png")));
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("login/LoginView.fxml"));
			AnchorPane rootLayout = loader.load();
			
			Scene scene = new Scene(rootLayout);
			stgLogin.setScene(scene);
			
			stgLogin.setOnCloseRequest(e->{
				try {
					Database.getInstance().DBCloseConnection();
				}catch(Exception err) {
					err.printStackTrace();
				}
			});
			
			stgLogin.show();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public static void showUser() {
		try {
			stgLogin.close();
			stgUser = new Stage();
			stgUser.setResizable(false);
			stgUser.setTitle("Three Seven Payroll System");
			stgUser.getIcons().add(new Image(Main.class.getResourceAsStream("books.png")));
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("user/UserView.fxml"));
			AnchorPane rootLayout = loader.load();
			
			Scene scene = new Scene(rootLayout);
			stgUser.setScene(scene);
			
			stgUser.setOnCloseRequest(e->{
				if(Dialogs.showConfirmMessage("Exit", "Close Application?",stgUser)) {
					try {
						Database.getInstance().DBCloseConnection();
					}catch(Exception err) {
						err.printStackTrace();
					}
				}
				else e.consume();
			});
			
			stgUser.show();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public static void loadScreens() {
		try {
			stgLogin.close();
			stgMenu = new Stage();
			stgMenu.setTitle("Three Seven Payroll System");
			stgMenu.getIcons().add(new Image(Main.class.getResourceAsStream("books.png")));
			stgMenu.setResizable(false);
			ScreensController container = new ScreensController();
			container.loadScreen(payrollID, payrollFile);
			container.loadScreen(employeesID, employeesFile);
			container.loadScreen(companiesID, companiesFile);
			container.loadScreen(unitsID, unitsFile);
			container.loadScreen(salaryID, salaryFile);
			container.loadScreen(deductionID, deductionFile);
			
			container.setScreen(payrollID);
			
			Group root = new Group();
	        root.getChildren().addAll(container);
			Scene scene = new Scene(root);
			stgMenu.setScene(scene);
			
			stgMenu.setOnCloseRequest(e->{
				if(Dialogs.showConfirmMessage("Exit", "Close Application?",stgMenu)) {
					try {
						Database.getInstance().DBCloseConnection();
					}catch(Exception err) {
						err.printStackTrace();
					}
				}
				else e.consume();
			});
			
			stgMenu.show();
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		LauncherImpl.launchApplication(Main.class, CustomPreloader.class, args);
	}
	
	public static Stage getStgMenu() {
		return stgMenu;
	}
	
	public static Stage getStgLogin() {
		return stgLogin;
	}
	
	public static Stage getStgUser() {
		return stgUser;
	}
	
	public static String getPayrollID() {
		return payrollID;
	}
	
	public static String getEmployeesID() {
		return employeesID;
	}
	
	public static String getCompaniesID() {
		return companiesID;
	}
	
	public static String getUnitsID() {
		return unitsID;
	}
	
	public static String getSalaryID() {
		return salaryID;
	}
	
	public static String getDeductionID() {
		return deductionID;
	}
}
