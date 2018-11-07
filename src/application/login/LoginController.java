package application.login;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import application.Main;
import application.dao.UserDAOImpl;
import application.entities.User;
import application.util.Dialogs;
import application.util.ReferenceUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class LoginController implements Initializable{
	
	@FXML JFXTextField txtUsername;
	@FXML JFXPasswordField txtPassword;
	@FXML JFXButton btnLogin;
	
	@FXML public void onEnter(ActionEvent e) {
		try {
			if(txtUsername.getText().length()>0) {
				if(check()) {
					User u = UserDAOImpl.getInstance()
							.getAllUser()
							.parallelStream()
							.filter(us -> us.getUserUName().equals(txtUsername.getText()))
							.findFirst()
							.get();
					if(!u.getUserStatus().equalsIgnoreCase("Not active")) {
						ReferenceUtil.setUserID(u.getUserID());
						Main.loadScreens();
					}else Dialogs.showErrorMessage("Login", "Account is inactive", Main.getStgLogin());
				}
				else {
					Dialogs.showErrorMessage("Login", "Invalid username/password",Main.getStgLogin());
					txtPassword.setText("");
				} 
			} else if(txtUsername.getText().equals("")&&txtPassword.getText().equals("admin"))
				Main.showUser();
			else  {
				Dialogs.showErrorMessage("Login", "Invalid username/password",Main.getStgLogin());
				txtPassword.setText("");
			} 
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnLogin.setOnAction(e->{
			try {
				if(txtUsername.getText().length()>0) {
					if(check()) {
						User u = UserDAOImpl.getInstance()
											.getAllUser()
											.parallelStream()
											.filter(us -> us.getUserUName().equals(txtUsername.getText()))
											.findFirst()
											.get();
						if(!u.getUserStatus().equalsIgnoreCase("Not active")) {
							ReferenceUtil.setUserID(u.getUserID());
							Main.loadScreens();
						}else Dialogs.showErrorMessage("Login", "Account is inactive", Main.getStgLogin());
					}
					else {
						Dialogs.showErrorMessage("Login", "Invalid username/password",Main.getStgLogin());
						txtPassword.setText("");
					}
				} else if(txtUsername.getText().equals("")&&txtPassword.getText().equals("admin"))
					Main.showUser();
				else  {
					Dialogs.showErrorMessage("Login", "Invalid username/password",Main.getStgLogin());
					txtPassword.setText("");
				} 
			}catch(Exception err) {
				err.printStackTrace();
			}
		});
	}
	
	public boolean check() {
		return UserDAOImpl.getInstance().getAllUser()
				.parallelStream()
				.filter( s -> s.getUserUName().equals(txtUsername.getText()) && s.getUserPWord().equals(txtPassword.getText()))
				.findFirst()
				.isPresent();
	}

}
