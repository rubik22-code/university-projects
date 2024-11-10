module org.example.auctionadanalytics {
    requires javafx.controls;
    requires javafx.swing;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.apache.logging.log4j;

    opens uk.ac.soton.app to javafx.fxml;
    opens uk.ac.soton.app.controller to javafx.fxml;

    exports uk.ac.soton.app;
    exports uk.ac.soton.app.controller;
    exports uk.ac.soton.app.model;
    exports uk.ac.soton.app.exceptions;
    exports uk.ac.soton.app.enums;
    exports uk.ac.soton.app.model.DataBase;
    exports uk.ac.soton.app.model.TimeUnits;
    exports uk.ac.soton.app.model.CSVDataUnits;
    exports uk.ac.soton.app.model.ProcessedData;
    exports uk.ac.soton.app.model.CSVData;
    opens uk.ac.soton.app.model to javafx.fxml;
}