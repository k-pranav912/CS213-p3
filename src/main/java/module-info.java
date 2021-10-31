module com.studentroster {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.studentroster to javafx.fxml;
    exports com.studentroster;
}