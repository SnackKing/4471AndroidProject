package com.example.alleg.a4471project;

import java.time.LocalDateTime;
import java.util.Date;

public class Call_Log {

    public String contactName;
    public String opposingNumber;
    public Date callStart;
    public String callLength;
    public String callDir;


    public Call_Log(){
        // Default constructor required for calls to DataSnapshot.getValue(Call_Log.class)
    }



    public Call_Log(String contactName, String opposingNumber, Date callStart, String callDuration, String callDir){
        this.contactName = contactName;
        this.opposingNumber = opposingNumber;
        this.callStart = callStart;
        this.callLength = callDuration;
        this.callDir = callDir;
    }
    public Call_Log(String opposingNumber, Date callStart, String callDuration, String callDir){
        this.contactName = contactName;
        this.opposingNumber = opposingNumber;
        this.callStart = callStart;
        this.callLength = callDuration;
        this.callDir = callDir;
    }
}