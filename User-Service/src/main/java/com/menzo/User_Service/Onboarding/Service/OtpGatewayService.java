package com.menzo.User_Service.Onboarding.Service;

//import com.menzo.User_Service.Global.Dto.Response.ApiResponse;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.OtpServiceException;
import com.menzo.User_Service.Global.FeignClient.Dto.*;
import com.menzo.User_Service.Global.Util.MaskingUtil;
import com.menzo.User_Service.Global.FeignClient.CommunicationClient;
import com.menzo.User_Service.Onboarding.Dto.Response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpGatewayService {

    private static final String SEND_FAILED_MESSAGE =
            "We couldn't send the verification code right now. Your account is saved; please request a new code in a moment.";
    private static final String VERIFY_UNAVAILABLE_MESSAGE =
            "We couldn't verify your code right now. Please try again in a moment.";

    private final CommunicationClient communicationClient;

    public SendOtpResponse sendSignupOtp(String email, String firstName) {
        String masked = MaskingUtil.maskEmail(email);
        log.info("Requesting signup OTP from communication-service for {}", masked);
        try {
            ApiResponse<SendOtpResponse> response =
                    communicationClient.sendOtp(new SendOtpRequest(email, firstName, OtpPurpose.SIGNUP));
            if (response == null || !response.success() || response.data() == null) {
                throw new OtpServiceException(ErrorCode.OTP_SEND_FAILED, SEND_FAILED_MESSAGE);
            }
            log.info("OTP dispatched for {}", masked);
            return response.data();
        } catch (OtpServiceException ex) {
            throw ex;                                   // already mapped (rate limit, 4xx/5xx from upstream)
        } catch (RuntimeException ex) {                 // timeouts, connection refused, no Eureka instance, bad JSON...
            log.error("communication-service unreachable or failed for {}", masked, ex);
            throw new OtpServiceException(ErrorCode.OTP_SEND_FAILED, SEND_FAILED_MESSAGE, ex);
        }
    }

    public void verifySignupOtp(String email, String otp) {
        String masked = MaskingUtil.maskEmail(email);
        log.info("Verifying signup OTP with communication-service for {}", masked);   // OTP itself is never logged
        try {
            ApiResponse<OtpVerifyResponse> response =
                    communicationClient.verifyOtp(new OtpVerifyRequest(email, otp, OtpPurpose.SIGNUP));
            if (response == null || !response.success()) {
                throw new OtpServiceException(ErrorCode.OTP_VERIFY_UNAVAILABLE, VERIFY_UNAVAILABLE_MESSAGE);
            }
        } catch (OtpServiceException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            log.error("communication-service unreachable or failed while verifying OTP for {}", masked, ex);
            throw new OtpServiceException(ErrorCode.OTP_VERIFY_UNAVAILABLE, VERIFY_UNAVAILABLE_MESSAGE, ex);
        }
    }

}
