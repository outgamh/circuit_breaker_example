package com.example.circuit_breaker_demo.data;

public class DatosLogin {

    private String login;
    private String pass;
    private AttributesDto attributes;

    public DatosLogin(){
    }

    public DatosLogin(String login, String pass, AttributesDto attributes){
        this.login = login;
        this.pass = pass;
        this.attributes = attributes;
    }

    public void setLogin(String login){this.login = login;}

    public void setPass(String pass){this.pass = pass;}

    public void setAttributes(AttributesDto attributes) {this.attributes = attributes;}

    public String getLogin() {
        return login;
    }

    public String getPass(String pass){return pass;}

    public AttributesDto getAttributes(){return attributes;}
}
