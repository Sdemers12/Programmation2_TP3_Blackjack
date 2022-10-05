module programmation.projet3_samuel_demers {
    requires javafx.controls;
    requires javafx.fxml;


    opens programmation.projet3_samuel_demers to javafx.fxml;
    exports programmation.projet3_samuel_demers;
}