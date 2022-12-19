package com.thales.wazajfx;

import com.thales.wazajfx.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;

public class WazaApplication extends Application {

    public static AnchorPane root;
    public static Map<String, Node> screens = new HashMap<>();
    private static String currentScreen = "userConnect";
    public static int APPWIDTH = 900;
    public static int APPLENGHT = 500;
    private static User connectedUser;


    @Override
    public void start(Stage stage) throws IOException {
        root = (AnchorPane) FXMLLoader.load(getClass().getResource("root.fxml"));
        screens.put("userConnect",(BorderPane) FXMLLoader.load(getClass().getResource("userConnect.fxml")));

        root.getChildren().add(screens.get(currentScreen));

        Scene scene = new Scene(root, APPWIDTH, APPLENGHT);
        stage.setTitle("Waza");
        System.out.println(getUser());
        stage.setScene(scene);
        stage.show();
    }
    public static void setScreen(String screen){

        if (!screens.keySet().contains(screen)) {
            try {
                screens.put(screen, (BorderPane) FXMLLoader.load(WazaApplication.class.getResource(screen + ".fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        root.getChildren().remove(screens.get(currentScreen));
        root.getChildren().add(screens.get(screen));
        currentScreen = screen;
    }

    public static void setUser(User user){
        connectedUser = user;
    }

    public static User getUser(){
        return connectedUser;
    }


    public static void main(String[] args) {
        launch();
    }
}