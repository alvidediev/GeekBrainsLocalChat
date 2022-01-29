module com.example.gblocalchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gblocalchat to javafx.fxml;
    exports com.example.gblocalchat;
}