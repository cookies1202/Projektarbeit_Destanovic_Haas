package service;

import model.Account;

import java.util.HashMap;

public class AccountManager {
    /**
     * Diese Hashmap speichter unseren User mit dem Benutzernamen als Schlüssel
     */
    private HashMap<String, Account> accounts;

    /**
     * Diese Konstruktor initalisiert unsere Hashmap und erstellt einen Standard Benutzer.
     */
    public AccountManager() {
        accounts = new HashMap<>();
        if (accounts.isEmpty()) {
            createAccount("root", "root");
        }
    }

    /**
     * Diese Funktion erstellt einen User.
     *
     * @param username
     * @param password
     * @return Bei False war der User bereits vorhanden.
     */
    public boolean createAccount(String username, String password) {
        if (accounts.containsKey(username)) {
            return false;
        }
        accounts.put(username, new Account(username, password));
        return true;
    }

    /**
     * Hier wird der Login durchegführt.
     *
     * @param username
     * @param password
     * @return zeigt ob es funktioniert hat.
     */
    public boolean login(String username, String password) {
        Account account = accounts.get(username);
        if (account != null) {
            return account.checkPassword(password);
        }
        return false;
    }

}

