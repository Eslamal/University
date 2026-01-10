package com.example.university.data;

public class Message {
    private String text;
    private boolean isSentByMe; // لو true يبقى أنا المرسل، false يبقى البوت

    public Message(String text, boolean isSentByMe) {
        this.text = text;
        this.isSentByMe = isSentByMe;
    }

    public String getText() {
        return text;
    }

    public boolean isSentByMe() {
        return isSentByMe;
    }
}