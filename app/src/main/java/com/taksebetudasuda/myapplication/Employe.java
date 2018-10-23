package com.taksebetudasuda.myapplication;

public class Employe extends Item{

    private String title;
    private String email;
    private String phone;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    public Employe() {

    }

    @Override
    public String toString() {
        return "Employe{" +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public Employe(int id, String name, String title, String email, String phone, int parentId) {

        this.title = title;
        this.email = email;
        this.phone = phone;
    }
}
