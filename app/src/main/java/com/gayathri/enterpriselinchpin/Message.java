package com.gayathri.enterpriselinchpin;

import android.util.Log;

public class Message {

    public String email;
    public boolean received;
    public String message;

    public Message(String email, String message, boolean received) {
        this.email = email;
        this.received = received;
        this.message = message;
    }

}
