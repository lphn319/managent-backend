package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.auth.AuthResponse;
import hcmute.lp.backend.model.dto.auth.LoginRequest;

public interface AuthService {
    AuthResponse login (LoginRequest loginRequest);
}
