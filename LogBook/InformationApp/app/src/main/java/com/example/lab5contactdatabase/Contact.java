package com.example.lab5contactdatabase;

public class Contact {
    private String name;
    private String email;
    private String dob;
    private String avatarUri ;

    public Contact(String name, String email, String dob, String avatarUri) {
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.avatarUri = avatarUri;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getAvatarUri() {
        return avatarUri;
    }
}