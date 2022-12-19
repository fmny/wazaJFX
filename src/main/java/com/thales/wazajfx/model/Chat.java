package com.thales.wazajfx.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
//@RequiredArgsConstructor
public class Chat {


    private int id;

    private String name;


    private List<Message> messages=new ArrayList<>();


    private List<User> users=new ArrayList<>();

}
