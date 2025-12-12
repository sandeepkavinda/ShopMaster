package DTO;

import java.sql.Timestamp;

public class VerificationCodeData {
    
    private String verificationCode;
    private Timestamp expiryTimestamp;

    public VerificationCodeData(String verificationCode, Timestamp expiryTimestamp) {
        this.verificationCode = verificationCode;
        this.expiryTimestamp = expiryTimestamp;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public Timestamp getExpiryTimestamp() {
        return expiryTimestamp;
    }
    
}
