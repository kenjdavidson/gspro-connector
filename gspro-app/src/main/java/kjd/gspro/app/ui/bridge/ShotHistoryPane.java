package kjd.gspro.app.ui.bridge;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import kjd.gspro.app.bridge.shot.Shot;
import kjd.gspro.app.bridge.shot.ShotHistoryService;
import kjd.gspro.data.Club;
import kjd.gspro.data.Hand;
import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class ShotHistoryPane implements Initializable {

    ShotHistoryService shotHistoryService;

    @FXML 
    TableView<Shot> shotTable;

    @FXML
    Button clearButton;

    @FXML 
    Button exportButton;

    public ShotHistoryPane(ShotHistoryService shotHistoryService) {
        this.shotHistoryService = shotHistoryService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearButton.disableProperty().bind(Bindings.isEmpty(shotTable.getItems()));
        exportButton.disableProperty().bind(Bindings.isEmpty(shotTable.getItems()));

        initializeTable();        
    }    

    @SuppressWarnings("unchecked")
    private void initializeTable() {
        shotTable.setItems(shotHistoryService.getShotHistory());
        shotTable.setPlaceholder(new Label(""));
        shotTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        TableColumn<Shot, Hand> handColumn = createColumn("Hand", value-> value.getValue().getHand());
        TableColumn<Shot, Club> clubColumn = createColumn("Club", value-> value.getValue().getClub());
        TableColumn<Shot, Number> ballSpeedColumn = createColumn("Speed", value -> value.getValue().getBallSpeed());
        TableColumn<Shot, Number> spinAxisColumn = createColumn("Axis", value -> value.getValue().getSpinAxis());
        TableColumn<Shot, Number> totalSpinColumn = createColumn("Spin", value -> value.getValue().getTotalSpin());
        TableColumn<Shot, Number> backSpinColumn = createColumn("Back", value -> value.getValue().getBackSpin());
        TableColumn<Shot, Number> sideSpinColumn = createColumn("Side", value -> value.getValue().getSideSpin());
        TableColumn<Shot, Number> hlaColumn = createColumn("HLA", value -> value.getValue().getHla());
        TableColumn<Shot, Number> vlaColumn = createColumn("VLA", value -> value.getValue().getVla());
        TableColumn<Shot, Number> clubSpeedColumn = createColumn("Speed", value -> value.getValue().getClubSpeed());
        TableColumn<Shot, Number> attackAngleColumn = createColumn("Attack", value -> value.getValue().getAttackAngle());
        TableColumn<Shot, Number> faceAngleColumn = createColumn("Face", value -> value.getValue().getFaceAngle());

        TableColumn<Shot, ?> playerColumns = new TableColumn<>("Player Info");
        playerColumns.getColumns().addAll(
            handColumn,
            clubColumn
        );

        TableColumn<Shot, ?> ballColumns = new TableColumn<>("Ball Info");
        ballColumns.getColumns().addAll(ballSpeedColumn,
            spinAxisColumn,
            totalSpinColumn,
            backSpinColumn,
            sideSpinColumn,
            hlaColumn,
            vlaColumn);

        TableColumn<Shot, ?> clubColumns = new TableColumn<>("Club Info");
        clubColumns.getColumns().addAll(
            clubSpeedColumn,
            attackAngleColumn,
            faceAngleColumn);

        shotTable.getColumns().addAll(playerColumns, ballColumns, clubColumns);
    }

    <T> TableColumn<Shot, T> createColumn(String title, 
                                            Callback<CellDataFeatures<Shot, T>, ObservableValue<T>> factory) {
        TableColumn<Shot, T> column = new TableColumn<>(title);
        column.setCellValueFactory(factory);
        return column;
    }

    @FXML
    public void clearHistory(Event event) {
        log.debug("Clearing shot history data");
        shotTable.getItems().clear();
    }

    @FXML 
    public void export(Event event) {
        log.debug("Request to export shot history data");
    }
}
