package service;
import model.Account;
import java.util.HashMap;

public class AccountManager {

    private static AccountManager instance;

    private HashMap<String, Account> accounts;

    public AccountManager() {
        accounts = new HashMap<>();
        if (accounts.isEmpty()) {
            createAccount("a", "a");
        }
    }

    public boolean createAccount(String username, String password) {
        if (accounts.containsKey(username)) {
            return false;
        }
        accounts.put(username, new Account(username, password));
        return true;
    }
    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }
    public boolean login(String username, String password) {
        Account account = accounts.get(username);
        return account != null && account.checkPassword(password);
    }
}
