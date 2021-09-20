module com.example.spaceinvaders {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.spaceinvaders to javafx.fxml;
    exports com.example.spaceinvaders;
}