module com.example.snake {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    // requires org.xerial.sqlitejdbc;


    opens com.example.snake to javafx.fxml;
    exports com.example.snake;
}