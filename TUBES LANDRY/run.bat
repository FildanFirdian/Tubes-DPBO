@echo off
set LIBS=lib\javafx.base.jar;lib\javafx.controls.jar;lib\javafx.graphics.jar;lib\javafx.fxml.jar
set CP=bin;lib\mysql-connector-j-8.4.0.jar;%LIBS%
java --module-path %LIBS% --add-modules javafx.controls,javafx.fxml -cp %CP% -Dprism.order=sw main.Launcher
