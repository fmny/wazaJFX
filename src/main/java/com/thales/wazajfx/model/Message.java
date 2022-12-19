package com.thales.wazajfx.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Message {


    private int id;

    @NonNull
    private User author;

    @NonNull
    private String contents;


    private List<User> receivers=new ArrayList<>();

    private Chat chat;

}
