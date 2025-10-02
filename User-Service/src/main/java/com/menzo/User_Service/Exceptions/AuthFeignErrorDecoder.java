package com.menzo.User_Service.Exceptions;

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
        try(Reader reader = response.body().asReader()) {
            String body = CharStreams.toString(reader);
            int status = response.status();

            return new AuthFeignException(status, body);
        } catch (IOException e) {
            return defaultDecoder.decode(methodKey, response);
        }
    }

}
