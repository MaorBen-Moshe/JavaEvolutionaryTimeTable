package bestItemComponent;

import javafx.beans.property.SimpleStringProperty;

public class CellItem {

    private final SimpleStringProperty serialItem;
    private final SimpleStringProperty subject;

    public CellItem(String item, String subject){
        serialItem = new SimpleStringProperty(item);
        this.subject = new SimpleStringProperty(subject);
    }

    public String getSerialItem() {
        return serialItem.get();
    }

    public String getSubject() {
        return subject.get();
    }
}
