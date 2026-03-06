package com.onehealth.kernel.authorization;

import com.onehealth.kernel.auth.TenantContext;
import com.onehealth.kernel.authorization.dto.CreatePermissionRequest;
import com.onehealth.kernel.authorization.dto.CreateRoleRequest;
import com.onehealth.kernel.authorization.dto.PermissionResponse;
import com.onehealth.kernel.authorization.dto.RoleResponse;
import com.onehealth.kernel.authorization.model.Permission;
import com.onehealth.kernel.authorization.model.Role;
import com.onehealth.kernel.authorization.repository.*;
import com.onehealth.kernel.authorization.service.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    @Mock RoleRepository roleRepository;
    @Mock PermissionRepository permissionRepository;
    @Mock RolePermissionRepository rolePermissionRepository;
    @Mock UserRoleRepository userRoleRepository;

    @InjectMocks
    AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId("TENANT_001");
    }

    @Test
    void createRole_shouldSucceed_whenNameIsUnique() {
        CreateRoleRequest request = new CreateRoleRequest("ADMIN", "Administrator role");
        when(roleRepository.existsByNameAndTenantId("ADMIN", "TENANT_001")).thenReturn(false);

        Role saved = new Role();
        saved.setName("ADMIN");
        saved.setDescription("Administrator role");
        when(roleRepository.save(any(Role.class))).thenReturn(saved);

        RoleResponse response = authorizationService.createRole(request);

        assertThat(response.name()).isEqualTo("ADMIN");
        assertThat(response.description()).isEqualTo("Administrator role");
    }

    @Test
    void createRole_shouldThrow_whenNameAlreadyExists() {
        CreateRoleRequest request = new CreateRoleRequest("ADMIN", "desc");
        when(roleRepository.existsByNameAndTenantId("ADMIN", "TENANT_001")).thenReturn(true);

        assertThatThrownBy(() -> authorizationService.createRole(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void createPermission_shouldSucceed_whenNameIsUnique() {
        CreatePermissionRequest request = new CreatePermissionRequest(
                "USER_READ", "USER", "READ", "Read user data");
        when(permissionRepository.existsByNameAndTenantId("USER_READ", "TENANT_001")).thenReturn(false);

        Permission saved = new Permission();
        saved.setName("USER_READ");
        saved.setResource("USER");
        saved.setAction("READ");
        when(permissionRepository.save(any(Permission.class))).thenReturn(saved);

        PermissionResponse response = authorizationService.createPermission(request);

        assertThat(response.name()).isEqualTo("USER_READ");
        assertThat(response.resource()).isEqualTo("USER");
        assertThat(response.action()).isEqualTo("READ");
    }

    @Test
    void listRoles_shouldReturnTenantRoles() {
        Role role1 = new Role();
        role1.setName("ADMIN");
        Role role2 = new Role();
        role2.setName("USER");

        when(roleRepository.findAllByTenantId("TENANT_001")).thenReturn(List.of(role1, role2));

        List<RoleResponse> result = authorizationService.listRoles();

        assertThat(result).hasSize(2);
        assertThat(result.stream().map(RoleResponse::name)).contains("ADMIN", "USER");
    }

    @Test
    void deleteRole_shouldThrow_whenRoleIsSystemRole() {
        UUID roleId = UUID.randomUUID();
        Role systemRole = new Role();
        systemRole.setName("SYSTEM_ADMIN");
        systemRole.setSystemRole(true);

        when(roleRepository.findByIdAndTenantId(roleId, "TENANT_001"))
                .thenReturn(Optional.of(systemRole));

        assertThatThrownBy(() -> authorizationService.deleteRole(roleId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("System roles cannot be deleted");
    }
}
