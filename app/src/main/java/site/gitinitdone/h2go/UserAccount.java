package site.gitinitdone.h2go;

/**
 * Created by surajmasand on 2/25/17.
 */

public class UserAccount {

    public enum AccountType {
        USER, WORKER, MANAGER, ADMIN;
    }

    private String username;
    private String title;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private boolean locked;
    private boolean banned;
    private AccountType userType;

    public UserAccount(String username, String title, String fName, String lName, String address, String email, AccountType type) {
        this.username = username;
        this.title = title;
        this.firstName = fName;
        this.lastName = lName;
        this.address = address;
        this.email = email;
        this.locked = false;
        this.banned = false;
        this.userType = type;
    }

}
