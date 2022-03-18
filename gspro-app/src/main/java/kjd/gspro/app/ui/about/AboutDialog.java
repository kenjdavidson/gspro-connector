package kjd.gspro.app.ui.about;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Separator;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AboutDialog extends Dialog<Void> implements Initializable {

	public AboutDialog() {
		super();

		initialize(null, ResourceBundle.getBundle("i18n"));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTitle(resources.getString("about.title"));

		ButtonType ok = new ButtonType(resources.getString("app.ok"), ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().add(ok);	

		VBox layout = new VBox(8);
		getDialogPane().setContent(layout);

		layout.setPadding(new Insets(16, 16, 16, 16));
		layout.setMaxWidth(300);

		Text content = new Text(resources.getString("about.text"));
		content.setWrappingWidth(layout.getMaxWidth());
		content.maxWidth(layout.getMaxWidth());
		layout.getChildren().add(content);
		layout.getChildren().add(new Separator(Orientation.HORIZONTAL));
		layout.getChildren().add(new Hyperlink(resources.getString("about.links.project")));
		layout.getChildren().add(new Hyperlink(resources.getString("about.links.gsproapi")));
		
	}
    
}
