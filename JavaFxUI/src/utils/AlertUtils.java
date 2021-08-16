package utils;

import javafx.scene.control.Alert;

public class AlertUtils {
    private final static Alert alert;

    static {
        alert = new Alert(Alert.AlertType.NONE);
    }

    public static void displayAlert(Alert.AlertType type, String header, String message){
        alert.setAlertType(type);
        alert.setResizable(true);
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.show();
    }
}
