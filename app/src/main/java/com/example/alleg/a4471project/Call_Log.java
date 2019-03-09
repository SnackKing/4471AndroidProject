package com.example.alleg.a4471project;

import java.time.LocalDateTime;

public class Call_Log {

    public String contactName;
    public String opposingNumber;
    public LocalDateTime callStart;
    public LocalDateTime callEnd;

    public Call_Log(){
        // Default constructor required for calls to DataSnapshot.getValue(Call_Log.class)
    }

    public Call_Log(String opposingNumber, LocalDateTime callStart, LocalDateTime callEnd){
        this.opposingNumber = opposingNumber;
        this.callStart = callStart;
        this.callEnd = callEnd;
    }

    public Call_Log(String contactName, String opposingNumber, LocalDateTime callStart, LocalDateTime callEnd){
        this.contactName = contactName;
        this.opposingNumber = opposingNumber;
        this.callStart = callStart;
        this.callEnd = callEnd;
    }
}
