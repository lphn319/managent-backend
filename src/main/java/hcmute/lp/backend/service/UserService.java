package hcmute.lp.backend.service;

import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;

import java.util.List;

public interface UserService {
    UserDto createUser(UserRequest userRequest);
    UserDto updateUser(Long id, UserRequest userRequest);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    void deleteUser(Long id);
    UserDto getCurrentUser();
    void changeUserStatus(Long id, boolean isActive);
}