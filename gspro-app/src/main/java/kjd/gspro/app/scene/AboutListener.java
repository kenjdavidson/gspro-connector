package kjd.gspro.app.scene;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.scene.control.Dialog;
import kjd.gspro.app.FXMLManager;
import kjd.gspro.app.LocaleService;
import kjd.gspro.app.event.SceneEvent;

@Component
public class AboutListener {

    Dialog<String> dialog;
    FXMLManager fxmlManager;    
    LocaleService localeService;

    public AboutListener(FXMLManager fxmlManager, LocaleService localeService) {
        this.fxmlManager = fxmlManager;        
        this.localeService = localeService;
    }

    @EventListener(condition = "scene eq 'about'")
    public void onApplicationEvent(SceneEvent event) {
        dialog = dialog();
        dialog.show();
    } 
    
    private Dialog<String> dialog() {
        if (dialog == null) {
            dialog = new Dialog<String>();
            dialog.setTitle(localeService.getResources().getString("app.about.title"));
            dialog.setContentText(localeService.getResources().getString("app.about.content"));
        }

        return dialog;
    }
}
