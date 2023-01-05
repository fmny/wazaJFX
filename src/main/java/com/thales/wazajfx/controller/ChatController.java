package com.thales.wazajfx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;

import com.thales.wazajfx.WazaApplication;
import com.thales.wazajfx.model.Chat;
import com.thales.wazajfx.model.Message;
import com.thales.wazajfx.model.User;
import com.thales.wazajfx.utils.Util;
import javafx.animation.KeyFrame;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import com.thales.wazajfx.utils.HttpRequests;
import javafx.util.Duration;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class ChatController implements Initializable {
    @FXML
    public Label labelChatName;

    @FXML
    public TextArea txtMessageToSend;
    @FXML
    public Button btnDeco;
    @FXML
    public Button btnChangeChat;

    @FXML
    public ListView listViewMessageInChat;
    @FXML
    public ListView listUserChat;
    @FXML
    public Button btnSendMessage;

    private Chat chat = WazaApplication.getMyChat();

    private ObservableList<User> usersInChatObservable;

    private ObservableList<Message> messageInChatObservable;

    //utilisateur connecte
    private ObjectProperty<User> selectedUser = new SimpleObjectProperty<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        initializeButtons();
        initializeText();
    }

    private void initializeButtons() {

        this.btnDeco.setOnMouseClicked(mouseEvent -> {
            WazaApplication.setScreen("userConnect");
        });
        this.btnChangeChat.setOnMouseClicked(mouseEvent -> {
            WazaApplication.setScreen("accueil");
        });


        //Code Erwan
        this.btnSendMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Message messageSend = new Message(txtMessageToSend.getText());
            messageSend.setAuthor(WazaApplication.getConnectedUser());
            messageSend.setChat(chat);

            try {
                    String restUrl = "http://localhost:8080/waza/api/messages/";
                    URL urlRequest = new URL(restUrl);
                    HttpURLConnection httpRequest = (HttpURLConnection) urlRequest.openConnection();
                    httpRequest.setRequestMethod("POST");
                    httpRequest.setDoOutput(true);
                    ObjectMapper objectMapperAdd = new ObjectMapper();
                    objectMapperAdd.registerModule(new JavaTimeModule());
                    byte[] out = objectMapperAdd.writeValueAsString(messageSend).getBytes(StandardCharsets.UTF_8);
                    int length = out.length;

                    httpRequest.setFixedLengthStreamingMode(length);
                    httpRequest.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    httpRequest.connect();
                    try (OutputStream os = httpRequest.getOutputStream()) {
                        os.write(out);
                    }
                    httpRequest.disconnect();
                } catch (Exception e) {
                System.out.println("Error: " + e);
                }
            txtMessageToSend.clear();

            //je laisse 0.2s d'arrêt entre l'envoi et la réception du
            // message pour que la requête se fasse avant la réception
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            chargeMessage();
        });

    }


    private void initializeTableView() {

        chargeList();
        chargeMessage();
        listUserChat.setCellFactory(param->new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty){
                super.updateItem(item,empty);
                if (empty||item==null){
                    setText(null);
                }
                else {
                    setText(item.getPseudo());
                }
            }
        });

        listUserChat.getSelectionModel().selectedItemProperty().addListener((observableValue, o, t1) -> {
            selectedUser.setValue((User) t1);
        });


        //initialisation des messages du chat
        listViewMessageInChat.setCellFactory(param->new ListCell<Message>() {

            @Override
            protected void updateItem(Message item, boolean empty){
                super.updateItem(item,empty);
                if (empty||item==null){
                    setText(null);
                }
                else {
                    setText(item.getAuthor().getPseudo()+" "+item.getDateMessage().getYear()+"/"+
                            item.getDateMessage().getMonth().getValue()+"/"+
                            item.getDateMessage().getDayOfMonth()+" - "+
                            item.getDateMessage().getHour()+"H"+
                            item.getDateMessage().getMinute()+
                            " :\n"+item.getContents());

                }
            }
    });
    }

    private void chargeList() {
        GluonObservableList<User> gotList = HttpRequests.getAllUser(chat.getId());
        gotList.setOnSucceeded(connectStateEvent -> {
            this.usersInChatObservable= FXCollections.observableArrayList(gotList);
            listUserChat.getItems().addAll(usersInChatObservable);

        });
    }

    private void chargeMessage() {
        GluonObservableObject<Chat> myChat = HttpRequests.getAllMessageByChat(chat.getId());
        myChat.setOnSucceeded(connectStateEvent -> {
            this.messageInChatObservable= FXCollections.observableArrayList(myChat.get().getMessages());
            listViewMessageInChat.getItems().clear();

            listViewMessageInChat.getItems().addAll(messageInChatObservable);

        });
    }

    private void initializeText() {
        labelChatName.isVisible();
        this.labelChatName.setText("****");
        WazaApplication.connectedChatProperty().addListener((observableValue, chat, t1) -> {
            this.labelChatName.setText(WazaApplication.getMyChat().getName());
        });
        this.labelChatName.setText(WazaApplication.getMyChat().getName());
    }


}
