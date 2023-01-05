package com.thales.wazajfx.controller;
import com.gluonhq.connect.GluonObservableObject;

import com.thales.wazajfx.WazaApplication;
import com.thales.wazajfx.model.User;
import com.thales.wazajfx.utils.HttpRequests;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

//Test
import java.security.MessageDigest;

public class UserConnectController implements Initializable {
    @FXML
    public TextField txtLogin;
    @FXML
    public PasswordField txtPassWord;
    @FXML
    public Button btnConnect;
    public Label lbError;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnConnect.setOnMouseClicked(mouseEvent -> {
            GluonObservableObject<User> PotentialConnected =
                    HttpRequests.tryLogin(new User("",txtLogin.getText(), txtPassWord.getText()));

            PotentialConnected.setOnSucceeded(connectStateEvent -> {
                WazaApplication.setConnectedUser(PotentialConnected.get());
                WazaApplication.setScreen("accueil");
                txtPassWord.clear();
                txtLogin.clear();
                lbError.setVisible(false);
            });
            PotentialConnected.setOnFailed(connectStateEvent -> {
                lbError.setVisible(true);
            });
        });


    }
}
