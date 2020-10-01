package app;

import javafx.application.HostServices;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.MyMovie;

public class HyperlinkCell implements Callback<TableColumn<MyMovie, String>, TableCell<MyMovie, String>>{

    private static HostServices hostServices ;

    public static HostServices getHostServices() {
        return hostServices ;
    }

    public static void setHostServices(HostServices hostServices) {
        HyperlinkCell.hostServices = hostServices ;
    }

    @Override
    public TableCell call(final TableColumn<MyMovie, String> param) {

        final TableCell<MyMovie, String> cell = new TableCell<MyMovie, String>() {

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