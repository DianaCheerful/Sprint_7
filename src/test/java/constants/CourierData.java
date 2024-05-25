package constants;

import static constants.TestConstants.EMPTY_STRING;

public enum CourierData {
    CD_1("best_login", "Annie"),

    CD_2("best_login2", "Billy"),

    CD_3("best_login3", "Christine"),

    CD_4("best_login4", EMPTY_STRING);

    private final String login;
    private final String firstname;

    CourierData(String login, String firstname) {
        this.login = login;
        this.firstname = firstname;
    }

    public String getLogin() {
        return login;
    }


    public String getFirstname() {
        return firstname;
    }

}