package com.thales.wazajfx.utils;
import com.gluonhq.connect.GluonObservableList;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;
import com.gluonhq.connect.provider.RestClient;
import com.thales.wazajfx.model.*;


public class HttpRequests {
    public static GluonObservableObject<User> tryLogin(User login) {

        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/waza/api/users/connect")
                .connectTimeout(10000)
                .readTimeout(1000)
                .dataString(JsonUtils.getStringJson(login))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }


    public static GluonObservableList<User> getAllUser() {
        RestClient school = RestClient.create()
                .method("GET")
                .host("http://localhost:8080/waza/api/users")
                .connectTimeout(10000)
                .readTimeout(1000);
        return DataProvider.retrieveList(school.createListDataReader(User.class));
    }

    public static GluonObservableObject<User> addUser(User myUser) {
        RestClient client = RestClient.create()
                .method("POST")
                .host("http://localhost:8080/waza/api/users/")
                .connectTimeout(10000)
                .readTimeout(1000)
                .dataString(JsonUtils.getStringJson(myUser))
                .contentType("application/json");

        return DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }

    public static void deleteUser(String id) {
        RestClient client = RestClient.create()
                .method("DELETE")
                .host("http://localhost:8080/waza/api/users/" + String.valueOf(id))
                .connectTimeout(10000)
                .readTimeout(1000);
        DataProvider.retrieveObject(client.createObjectDataReader(User.class));
    }



}
