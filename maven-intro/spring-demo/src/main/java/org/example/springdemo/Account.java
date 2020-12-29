package org.example.springdemo;

/**
 * Аккаунт
 */
public class Account {
    //region username : String
    private String username;
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
    //endregion
    //region password : String
    private String password;

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
    //endregion

    @Override
    public String toString(){
        return "Account{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
