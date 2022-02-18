module com.example.gblocalchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.example.gblocalchat to javafx.fxml;
    exports com.example.gblocalchat;
}