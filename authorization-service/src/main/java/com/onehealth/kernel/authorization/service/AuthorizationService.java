package com.onehealth.kernel.authorization.service;

import com.onehealth.kernel.auth.TenantContext;
import com.onehealth.kernel.authorization.dto.*;
import com.onehealth.kernel.authorization.model.*;
import com.onehealth.kernel.authorization.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AuthorizationService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRoleRepository userRoleRepository;

    public AuthorizationService(RoleRepository roleRepository,
                                PermissionRepository permissionRepository,
                                RolePermissionRepository rolePermissionRepository,
                                UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRoleRepository = userRoleRepository;
    }

    // --- Role management ---

    public RoleResponse createRole(CreateRoleRequest request) {
        String tenantId = TenantContext.getTenantId();
        if (roleRepository.existsByNameAndTenantId(request.name(), tenantId)) {
            throw new IllegalArgumentException("Role '" + request.name() + "' already exists in this tenant");
        }
        Role role = new Role();
        role.setName(request.name());
        role.setDescription(request.description());
        role = roleRepository.save(role);
        return toRoleResponse(role);
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> listRoles() {
        String tenantId = TenantContext.getTenantId();
        return roleRepository.findAllByTenantId(tenantId).stream()
                .map(this::toRoleResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponse getRole(UUID roleId) {
        String tenantId = TenantContext.getTenantId();
        return roleRepository.findByIdAndTenantId(roleId, tenantId)
                .map(this::toRoleResponse)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }

    public void deleteRole(UUID roleId) {
        String tenantId = TenantContext.getTenantId();
        Role role = roleRepository.findByIdAndTenantId(roleId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        if (role.isSystemRole()) {
            throw new IllegalStateException("System roles cannot be deleted");
        }
        roleRepository.delete(role);
    }

    // --- Permission management ---

    public PermissionResponse createPermission(CreatePermissionRequest request) {
        String tenantId = TenantContext.getTenantId();
        if (permissionRepository.existsByNameAndTenantId(request.name(), tenantId)) {
            throw new IllegalArgumentException("Permission '" + request.name() + "' already exists in this tenant");
        }
        Permission permission = new Permission();
        permission.setName(request.name());
        permission.setResource(request.resource());
        permission.setAction(request.action());
        permission.setDescription(request.description());
        permission = permissionRepository.save(permission);
        return toPermissionResponse(permission);
    }

    @Transactional(readOnly = true)
    public List<PermissionResponse> listPermissions() {
        String tenantId = TenantContext.getTenantId();
        return permissionRepository.findAllByTenantId(tenantId).stream()
                .map(this::toPermissionResponse)
                .toList();
    }

    // --- Role-Permission assignment ---

    public void grantPermissionToRole(UUID roleId, UUID permissionId) {
        String tenantId = TenantContext.getTenantId();
        roleRepository.findByIdAndTenantId(roleId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        permissionRepository.findByIdAndTenantId(permissionId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));

        if (!rolePermissionRepository.existsByRoleIdAndPermissionId(roleId, permissionId)) {
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermissionId(permissionId);
            rolePermissionRepository.save(rp);
        }
    }

    public void revokePermissionFromRole(UUID roleId, UUID permissionId) {
        rolePermissionRepository.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }

    // --- User-Role assignment ---

    public void assignRoleToUser(UUID userId, UUID roleId) {
        String tenantId = TenantContext.getTenantId();
        roleRepository.findByIdAndTenantId(roleId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        if (!userRoleRepository.existsByUserIdAndRoleId(userId, roleId)) {
            UserRole ur = new UserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            userRoleRepository.save(ur);
        }
    }

    public void revokeRoleFromUser(UUID userId, UUID roleId) {
        userRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Transactional(readOnly = true)
    public boolean hasPermission(UUID userId, String resource, String action) {
        List<UserRole> userRoles = userRoleRepository.findAllByUserId(userId);
        for (UserRole ur : userRoles) {
            List<RolePermission> rps = rolePermissionRepository.findAllByRoleId(ur.getRoleId());
            for (RolePermission rp : rps) {
                boolean match = permissionRepository.findById(rp.getPermissionId())
                        .map(p -> p.getResource().equals(resource) && p.getAction().equals(action))
                        .orElse(false);
                if (match) return true;
            }
        }
        return false;
    }

    private RoleResponse toRoleResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(),
                role.isSystemRole(), role.getTenantId(), role.getCreatedAt());
    }

    private PermissionResponse toPermissionResponse(Permission perm) {
        return new PermissionResponse(perm.getId(), perm.getName(), perm.getResource(),
                perm.getAction(), perm.getDescription(), perm.getTenantId(), perm.getCreatedAt());
    }
}
