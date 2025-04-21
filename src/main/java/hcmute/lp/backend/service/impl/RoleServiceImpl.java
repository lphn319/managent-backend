package hcmute.lp.backend.service.impl;

import hcmute.lp.backend.exception.ResourceNotFoundException;
import hcmute.lp.backend.model.dto.role.RoleDto;
import hcmute.lp.backend.model.dto.role.RoleRequest;
import hcmute.lp.backend.model.entity.Role;
import hcmute.lp.backend.model.mapper.RoleMapper;
import hcmute.lp.backend.repository.RoleRepository;
import hcmute.lp.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toDtoList(roles);
    }

    @Override
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional
    public RoleDto createRole(RoleRequest roleRequest) {
        // Kiểm tra xem role đã tồn tại chưa
        if (roleRepository.existsByName(roleRequest.getName())) {
            throw new IllegalArgumentException("Role already exists with name: " + roleRequest.getName());
        }

        // Tạo mới role
        Role role = roleMapper.toEntity(roleRequest);
        Role savedRole = roleRepository.save(role);

        return roleMapper.toDto(savedRole);
    }

    @Override
    @Transactional
    public RoleDto updateRole(Long id, RoleRequest roleRequest) {
        // Kiểm tra xem role có tồn tại không
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));

        // Kiểm tra xem tên mới đã tồn tại chưa (nếu thay đổi tên)
        if (!role.getName().equals(roleRequest.getName()) &&
                roleRepository.existsByName(roleRequest.getName())) {
            throw new IllegalArgumentException("Role already exists with name: " + roleRequest.getName());
        }

        // Cập nhật role
        roleMapper.updateEntityFromRequest(roleRequest, role);
        Role updatedRole = roleRepository.save(role);

        return roleMapper.toDto(updatedRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        // Kiểm tra xem role có tồn tại không
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }

        // Xóa role
        roleRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return roleRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public RoleDto getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
        return roleMapper.toDto(role);
    }
}