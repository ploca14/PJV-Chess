package cz.cvut.fel.pjv.view;

import cz.cvut.fel.pjv.model.JoinRequest;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class JoinDialog extends Dialog<JoinRequest> {
    private TextField nameField = new TextField();
    private TextField ipField = new TextField();
    private TextField portField = new TextField();

    public JoinDialog() {
        this.setTitle("Join server");
        this.setHeaderText(null);
        DialogPane dialogPane = this.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        nameField.setPromptText("Username");
        ipField.setPromptText("IP address");
        portField.setPromptText("Port");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("IP Address:"), 0, 1);
        grid.add(ipField, 1, 1);
        grid.add(new Label("Port:"), 0, 2);
        grid.add(portField, 1, 2);

        dialogPane.setContent(grid);
        this.setResultConverter((ButtonType button) -> {
            if (button.equals(ButtonType.OK)) {
                if (validateFields()) {
                    JoinRequest joinRequest = null;
                    try {
                        joinRequest = new JoinRequest(nameField.getText(), ipField.getText(), portField.getText());
                    } catch (NumberFormatException exception) {
                        new Alert(Alert.AlertType.ERROR, "The port must be a number between 1 and 65535").show();
                    }
                    return joinRequest;
                }

            }
            return null;
        });
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You must specify a name").show();
            return false;
        }

        if(ipField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You must specify an IP address").show();
            return false;
        }

        if(portField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "You must specify a port").show();
            return false;
        }

        return true;
    }
}
