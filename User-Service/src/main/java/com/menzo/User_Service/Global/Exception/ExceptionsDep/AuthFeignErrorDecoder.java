package com.menzo.User_Service.Global.Exception.ExceptionsDep;

import com.google.common.io.CharStreams;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

@Component
public class AuthFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.body() == null) {
            return defaultDecoder.decode(methodKey, response);
        }
        try(Reader reader = response.body().asReader()) {
            String body = CharStreams.toString(reader);
            return new AuthFeignException(response.status(), body);
        } catch (IOException e) {
            return defaultDecoder.decode(methodKey, response);
        }
    }

}
