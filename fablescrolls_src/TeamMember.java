package com.example.fablescrolls;

public class TeamMember {
    private String name;
    private String role;
    private String bio;
    private int photoResId;

    public TeamMember(String name, String role, String bio, int photoResId) {
        this.name = name;
        this.role = role;
        this.bio = bio;
        this.photoResId = photoResId;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
    public String getBio() { return bio; }
    public int getPhotoResId() { return photoResId; }
}
