package site.gitinitdone.h2go;

/**
 * Created by surajmasand on 2/25/17.
 */

public class UserAccount {

    public enum AccountType {
        USER, WORKER, MANAGER, ADMIN;
        public String toString() {
            return this.name().charAt(0) + this.name().substring(1).toLowerCase();
        }
    }

    public enum Title {
        MR, MS, MRS, DR, NONE;
        public String toString() {
            return this.name().charAt(0) + this.name().substring(1).toLowerCase() + ".";
        }
    }

    private String username;
    private Title title;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private boolean locked;
    private boolean banned;
    private AccountType userType;

    public UserAccount(String username, Title title, String fName, String lName, String address, String email, AccountType type) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isBanned() {
        return banned;
    }

    public AccountType getUserType() {
        return userType;
    }

    public void setUserType(AccountType userType) {
        this.userType = userType;
    }

}
