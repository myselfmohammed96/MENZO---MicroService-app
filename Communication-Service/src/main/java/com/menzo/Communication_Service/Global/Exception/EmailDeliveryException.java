package com.menzo.Communication_Service.Global.Exception;

import com.menzo.Communication_Service.Global.Enum.ErrorCode;

public class EmailDeliveryException extends BusinessException {

    public EmailDeliveryException(String message, Throwable cause) {
        super(ErrorCode.EMAIL_DELIVERY_FAILED, message, cause);
    }

}
