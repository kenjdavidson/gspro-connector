package kjd.gspro.app.scene;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.scene.control.Dialog;
import kjd.gspro.app.ViewManager;
import kjd.gspro.app.LocaleManager;
import kjd.gspro.app.event.SceneEvent;

@Component
public class AboutListener {

    Dialog<String> dialog;
    ViewManager fxmlManager;    
    LocaleManager localeService;

    public AboutListener(ViewManager fxmlManager, LocaleManager localeService) {
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
