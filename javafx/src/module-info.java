module YutGameProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens View to javafx.fxml;
    exports View;
    exports Controller;
    exports Model;
}