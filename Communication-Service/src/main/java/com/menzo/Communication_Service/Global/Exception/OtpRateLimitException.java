package com.menzo.Communication_Service.Global.Exception;

import com.menzo.Communication_Service.Global.Enum.ErrorCode;

public class OtpRateLimitException extends BusinessException{

    public OtpRateLimitException(String message, long retryAfterSeconds) {
        super(ErrorCode.OTP_RATE_LIMITED, message);
        withRetryAfter(retryAfterSeconds);
    }

}
