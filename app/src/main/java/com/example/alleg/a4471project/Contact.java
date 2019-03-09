package com.example.alleg.a4471project;

public class Contact {

    public String full_name;
    public String phone_number;

    public Contact(){
        // Default constructor required for calls to DataSnapshot.getValue(Contact.class)
    }

    public Contact(String fullName, String phoneNumber){
        this.full_name = fullName;
        this.phone_number = phoneNumber;
    }

}
