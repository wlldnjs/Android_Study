package kimjiwon.sigongeducation.databindingex;

/**
 * Created by kimjiwon on 2017. 12. 19..
 */

public class UserData {
    private String userName;
    private String userMemo;

    public UserData(String userName, String userMemo) {
        this.userName = userName;
        this.userMemo = userMemo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMemo() {
        return userMemo;
    }

    public void setUserMemo(String userMemo) {
        this.userMemo = userMemo;
    }
}
