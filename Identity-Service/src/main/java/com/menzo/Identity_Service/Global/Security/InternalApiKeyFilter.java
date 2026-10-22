package com.menzo.Identity_Service.Global.Security;

import com.menzo.Identity_Service.Global.Exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Slf4j
public class InternalApiKeyFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-Internal-Key";

    private final byte[] expected;
    private final JsonErrorWriter errorWriter;

    public InternalApiKeyFilter(String apiKey, JsonErrorWriter errorWriter) {
        this.expected = apiKey.getBytes(StandardCharsets.UTF_8);
        this.errorWriter = errorWriter;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/internal/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String provided = request.getHeader(HEADER);
        if (provided == null || !MessageDigest.isEqual(provided.getBytes(StandardCharsets.UTF_8), expected)) {
            log.warn("Rejected internal call without a valid key: {} {} from {}",
                    request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
            errorWriter.write(response, ErrorCode.UNAUTHORIZED_INTERNAL_CALL, "Unauthorized internal call.");
            return;
        }
        chain.doFilter(request, response);
    }
}
