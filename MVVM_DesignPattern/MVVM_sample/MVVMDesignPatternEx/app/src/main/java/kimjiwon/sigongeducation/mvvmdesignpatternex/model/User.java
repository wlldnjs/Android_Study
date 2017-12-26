package kimjiwon.sigongeducation.mvvmdesignpatternex.model;

/**
 * Created by kimjiwon on 2017. 12. 22..
 */

public class User {
    private String userName;
    private String editTextData;
    private int userNumber;

    public void setUserName() {
        userName = editTextData;
    }

    public void setEditTextData(String editTextData) {
        this.editTextData = editTextData;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(int userNumber) {
        this.userNumber = userNumber;
    }
}
