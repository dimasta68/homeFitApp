package com.hardcoding.homework;



public class Post {

    private int userId;

    private String username;

    private String answer;




    public Post(String username, String answer) {
        this.username = username;
        this.answer = answer;


    }

    public String getUsername() {
        return username;
    }

    public String getAnswer() {
        return answer;
    }


}
