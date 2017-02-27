package site.gitinitdone.h2go;

/**
 * This class will create "UserAccount" objects that holds all the information that belongs to a
 * specific user, except the password for security reasons.
 */
public class UserAccount {

    /**
     * An enum for the different types of accounts (User, Worker, Manager, and Admin)
     * This will be used to determine who has access to what features.
     */
    public enum AccountType {
        USER, WORKER, MANAGER, ADMIN;
        public String toString() {
            // Capital first letter, followed by all lower case letters
            return this.name().charAt(0) + this.name().substring(1).toLowerCase();
        }
    }

    /**
     * An enum for the different titles a person could have (Mr., Ms., Mrs., Dr., and None.)
     * This is used to ensure that a valid title is selected when creating and editing an account.
     */
    public enum Title {
        MR, MS, MRS, DR, NONE;
        public String toString() {
            // Capital first letter, all lower case letters, followed by a dot "."
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

    /**
     * Constructor for the UserAccount, where locked and banned are defaulted to false
     * Uses constructor chaining.
     *
     * @param username the account's username
     * @param title the user's title
     * @param fName the user's first name
     * @param lName the user's last name
     * @param address the user's home address
     * @param email the user's email address
     * @param type the type of user account (User, Worker, etc)
     */
    public UserAccount(String username, Title title, String fName, String lName, String address,
                                String email, AccountType type) {
        this(username, title, fName, lName, address, email, false, false, type);
    }

    /**
     * Constructor for the UserAccount, where locked and banned are defaulted to false;
     *
     * @param username the account's username
     * @param title the user's title
     * @param fName the user's first name
     * @param lName the user's last name
     * @param address the user's home address
     * @param email the user's email address
     * @param locked whether of not the account is locked out
     * @param banned whether of not the account is banned from posting reports
     * @param type the type of user account (User, Worker, etc)
     */
    public UserAccount(String username, Title title, String fName, String lName, String address,
                                String email, boolean locked, boolean banned, AccountType type) {
        this.username = username;
        this.title = title;
        this.firstName = fName;
        this.lastName = lName;
        this.address = address;
        this.email = email;
        this.locked = locked;
        this.banned = banned;
        this.userType = type;
    }


    // ************************************************************
    // * The following methods are the getter methods             *
    // * for each of these properties of the user account         *
    // * There is no need for setters since we store everything   *
    // * in the online database                                   *
    // ************************************************************

    public String getUsername() {
        return username;
    }

    public Title getTitle() {
        return title;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
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

}
