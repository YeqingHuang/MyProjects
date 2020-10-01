package allday;

import javafx.application.HostServices;
import javafx.scene.control.*;
import javafx.util.Callback;

public class HyperlinkPersonCell implements Callback<TableColumn<Person, String>, TableCell<Person, String>>{

    private static HostServices hostServices ;

    public static HostServices getHostServices() {
        return hostServices ;
    }

    public static void setHostServices(HostServices hostServices) {
        HyperlinkPersonCell.hostServices = hostServices ;
    }

    @Override
    public TableCell call(final TableColumn<Person, String> param) {

        final TableCell<Person, String> cell = new TableCell<Person, String>() {

            final Hyperlink hyperlink = new Hyperlink();

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    hyperlink.setOnAction(event -> {
                        HostServices host = getHostServices();
                        host.showDocument(hyperlink.getText());
                    });
                    setGraphic(hyperlink);
                    hyperlink.setText(item);
                }
            }
        };
        return cell;
    }
}