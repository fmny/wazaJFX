package com.thales.wazajfx.controller;
import com.gluonhq.connect.GluonObservableObject;

import com.thales.wazajfx.WazaApplication;
import com.thales.wazajfx.model.User;
import com.thales.wazajfx.utils.HttpRequests;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UserConnectController implements Initializable {
    @FXML
    public TextField txtUser;

    //@FXML
    //public TextField txtPseudo;

    @FXML
    public TextField txtPassWord;
    @FXML
    public Button btnConnect;
    public Label lbError;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        btnConnect.setOnMouseClicked(mouseEvent -> {
            GluonObservableObject<User> PotentialConnected =
                    HttpRequests.tryLogin(new User("monPseudo",txtUser.getText(), txtPassWord.getText()));
//modif FM ici (ajout de txtPseudo
            PotentialConnected.setOnSucceeded(connectStateEvent -> {
               // SchoolManagingApplication.setUser(PotentialConnected.get());
                //SchoolManagingApplication.setScreen("accueil");
                WazaApplication.setUser(PotentialConnected.get());
                WazaApplication.setScreen("accueil");

            });
            PotentialConnected.setOnFailed(connectStateEvent -> {
                lbError.setVisible(true);
            });
        });


    }
}
