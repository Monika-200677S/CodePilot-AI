package CodePilotAI.dto;

public class ForgotPasswordResponse {
    private String message;
    // DEMO MODE ONLY: no SMTP server is configured in this project, so the
    // reset token is returned directly in the API response instead of being
    // emailed. Wire up a real mail sender (see README) before using this in
    // production, and stop returning the token here once you do.
    private String demoResetToken;

    public ForgotPasswordResponse() {}
    public ForgotPasswordResponse(String message, String demoResetToken) {
        this.message = message;
        this.demoResetToken = demoResetToken;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getDemoResetToken() { return demoResetToken; }
    public void setDemoResetToken(String demoResetToken) { this.demoResetToken = demoResetToken; }
}
