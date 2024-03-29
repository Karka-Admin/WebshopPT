package com.example.webshoppt.utils;

import com.example.webshoppt.model.AccountType;
import com.example.webshoppt.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserManager {
    private User user;
    private AccountType activeAccountType;
}
