package application.util;
import java.util.stream.Stream;

import com.jfoenix.controls.JFXComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
//import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
//import javafx.stage.Window;

/**
 * 
 * Uses a combobox tooltip as the suggestion for auto complete and updates the
 * combo box itens accordingly <br />
 * It does not work with space, space and escape cause the combobox to hide and
 * clean the filter ... Send me a PR if you want it to work with all characters
 * -> It should be a custom controller - I KNOW!
 * 
 * @author wsiqueir
 *
 * @param <T>
 */
public class ComboBoxAutoComplete<T> {

	private JFXComboBox<String> cmb;
	String filter = "";
	private ObservableList<String> originalItems;

	public ComboBoxAutoComplete(JFXComboBox<String> cmb) {
		this.cmb = cmb;
		originalItems = FXCollections.observableArrayList(cmb.getItems());
//		cmb.setTooltip(new Tooltip());
		cmb.setOnKeyPressed(this::handleOnKeyPressed);
		cmb.setOnHidden(this::handleOnHiding);
	}

	public void handleOnKeyPressed(KeyEvent e) {
		ObservableList<String> filteredList = FXCollections.observableArrayList();
		KeyCode code = e.getCode();

		if (code.isLetterKey()||code.isDigitKey()) {
			filter += e.getText();
		}
		if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
			filter = filter.substring(0, filter.length() - 1);
			cmb.getItems().setAll(originalItems);
		}
		if (code == KeyCode.ESCAPE) {
			filter = "";
		}
		if (filter.length() == 0) {
			filteredList = originalItems;
//			cmb.getTooltip().hide();
		} else {
			Stream<String> itens = cmb.getItems().stream();
			String txtUsr = filter.toString().toLowerCase();
			itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
//			cmb.getTooltip().setText(txtUsr);
//			Window stage = cmb.getScene().getWindow();
//			double posX = stage.getX() + cmb.getBoundsInParent().getMinX();
//			double posY = stage.getY() + cmb.getBoundsInParent().getMinY();
//			cmb.getTooltip().show(stage, posX, posY);
//			cmb.show();
		}
		cmb.getItems().setAll(filteredList);
	}

	public void handleOnHiding(Event e) {
		filter = "";
//		cmb.getTooltip().hide();
		String s = cmb.getSelectionModel().getSelectedItem();
		cmb.getItems().setAll(originalItems);
		cmb.getSelectionModel().select(s);
	}

}