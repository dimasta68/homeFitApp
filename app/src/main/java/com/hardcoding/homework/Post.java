package com.hardcoding.homework;



public class Post {

    private int userId;

    private String username;

    private String title_task;

    private String answer;
    private String status;




    public Post(String username, String answer,String status, String title_task) {
        this.username = username;
        this.answer = answer;
        this.status = status;
        this.title_task =title_task;


    }

    public String getUsername() {
        return username;
    }

    public String getAnswer() {
        return answer;
    }


}
