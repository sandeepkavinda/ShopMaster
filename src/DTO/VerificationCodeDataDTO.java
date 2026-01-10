package DTO;

import java.sql.Timestamp;

public class VerificationCodeDataDTO {
    
    private String verificationCode;
    private Timestamp expiryTimestamp;

    public VerificationCodeDataDTO(String verificationCode, Timestamp expiryTimestamp) {
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
