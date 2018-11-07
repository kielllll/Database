package application.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Dialogs {
	
	public static void showMessage(String header, String content, Stage stg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.initOwner(stg);

		alert.showAndWait();
	}
	
	public static void showErrorMessage(String header, String content,Stage stg) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.initOwner(stg);
		alert.showAndWait();
	}
	
	public static boolean showConfirmMessage(String header, String content, Stage stg) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.initOwner(stg);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK)
		    return true;
		return false;
	}
}
