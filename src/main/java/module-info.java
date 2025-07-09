module com.lemur {
    //REQUIRES
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires firebase.admin;
    requires com.google.api.apicommon;
    requires google.cloud.firestore;
    requires google.cloud.core;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires com.google.gson;
    requires java.prefs;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires javafx.media;
    requires org.slf4j;

    //GLOBAL
    opens com.lemur to javafx.fxml;
    exports com.lemur;
    opens com.lemur.GLOBAL.APIS to javafx.fxml;
    exports com.lemur.GLOBAL.APIS;
    opens com.lemur.GLOBAL.model to javafx.fxml;
    exports com.lemur.GLOBAL.model;
    opens com.lemur.GLOBAL.rutinas to javafx.fxml;
    exports com.lemur.GLOBAL.rutinas;
    opens com.lemur.GLOBAL.components to javafx.fxml;
    exports com.lemur.GLOBAL.components;
    opens com.lemur.GLOBAL.reporting to javafx.fxml;
    exports com.lemur.GLOBAL.reporting;
    opens com.lemur.GLOBAL.interfaces to javafx.fxml;
    exports com.lemur.GLOBAL.interfaces;

    //LAUNCHER
    opens com.lemur.LAUNCHER.repository to javafx.fxml;
    exports com.lemur.LAUNCHER.repository;
    opens com.lemur.LAUNCHER.controller to javafx.fxml;
    exports com.lemur.LAUNCHER.controller;

    //ROLLCALL
    opens com.lemur.ROLLCALL.controller to javafx.fxml;
    exports com.lemur.ROLLCALL.controller;
    opens com.lemur.ROLLCALL.repository to javafx.fxml;
    exports com.lemur.ROLLCALL.repository;

    //GEARMANAGER
    opens com.lemur.GEARMANAGER.controller to javafx.fxml;
    exports com.lemur.GEARMANAGER.controller;
    opens com.lemur.GEARMANAGER.repository to javafx.fxml;
    exports com.lemur.GEARMANAGER.repository;

    //FOODSAFE
    opens com.lemur.FOODSAFE.controller to javafx.fxml;
    exports com.lemur.FOODSAFE.controller;
    opens com.lemur.FOODSAFE.repository to javafx.fxml;
    exports com.lemur.FOODSAFE.repository;

    //HUB
    opens com.lemur.HUB.controller to javafx.fxml;
    exports com.lemur.HUB.controller;
    opens com.lemur.HUB.repository to javafx.fxml;
    exports com.lemur.HUB.repository;

}