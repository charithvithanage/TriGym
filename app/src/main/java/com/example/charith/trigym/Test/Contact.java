package com.example.charith.trigym.Test;

public class Contact {
    private String Name;
    private int Sync_status;

    public Contact(String name, int sync_status) {
        this.setName(name);
        this.setSync_status(sync_status);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getSync_status() {
        return Sync_status;
    }

    public void setSync_status(int sync_status) {
        Sync_status = sync_status;
    }
}
