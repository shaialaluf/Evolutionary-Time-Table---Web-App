package Problems;

public class UserCurrentResult {
    String username;
    CurrentResult currentResult;

    public UserCurrentResult(String username, CurrentResult currentResult) {
        this.username = username;
        this.currentResult = currentResult;
    }

    public CurrentResult getCurrentResult() {
        return currentResult;
    }

    public String getUsername() {
        return username;
    }
}
