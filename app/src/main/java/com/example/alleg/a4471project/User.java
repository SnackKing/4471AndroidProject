package com.example.alleg.a4471project;

import java.time.LocalDateTime;

public class User {

    public String date_of_birth;
    public String full_name;
    public String phone_number;
    public LocalDateTime join_date;
    public String user_id;

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String dateOfBirth, String fullName, String phoneNumber, LocalDateTime joinDate, String uid){
        this.date_of_birth = dateOfBirth;
        this.full_name = fullName;
        this.phone_number = phoneNumber;
        this.join_date = joinDate;
        this.user_id = uid;
    }
}