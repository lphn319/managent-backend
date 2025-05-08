package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.exception.UnauthorizedException;
import hcmute.lp.backend.model.common.CommonCategories;
import hcmute.lp.backend.model.dto.user.UserDto;
import hcmute.lp.backend.model.dto.user.UserRequest;
import hcmute.lp.backend.model.entity.Department;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.entity.User;
import hcmute.lp.backend.model.mapper.UserMapper;
import hcmute.lp.backend.repository.DepartmentRepository;
import hcmute.lp.backend.repository.RoleRepository;
import hcmute.lp.backend.repository.UserRepository;
import hcmute.lp.backend.security.SecurityUtils;
import hcmute.lp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

}