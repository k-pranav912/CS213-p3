module com.studentroster.cs213p3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.studentroster to javafx.fxml;
    exports com.studentroster;
}