package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.auth.AuthResponse;
import hcmute.lp.backend.model.dto.auth.LoginRequest;
import hcmute.lp.backend.model.dto.auth.RegisterRequest;
import org.apache.coyote.BadRequestException;

public interface AuthService {
    AuthResponse login (LoginRequest loginRequest);
    AuthResponse register(RegisterRequest registerRequest) throws BadRequestException;

}
