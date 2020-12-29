package org.example.springdemo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "basic-auth")
public class LoginsConf {
    private List<Account> accounts = new ArrayList<>();
    public List<Account> getAccounts(){ return accounts; }
    public void setAccounts(List<Account> accounts){ this.accounts = accounts; }
}
