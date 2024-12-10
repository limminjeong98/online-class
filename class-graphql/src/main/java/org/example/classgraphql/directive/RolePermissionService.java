package org.example.classgraphql.directive;

import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.example.classgraphql.exception.UnauthorizedException;
import org.example.classgraphql.model.CourseSession;
import org.example.classgraphql.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class RolePermissionService {

    private final Map<String, Set<PermissionAction>> rolePermissions = new HashMap<>();
    private final EnrollmentService enrollmentService;

    @Autowired
    public RolePermissionService(EnrollmentService enrollmentService) {
        initializeRoles();
        this.enrollmentService = enrollmentService;
    }

    private void initializeRoles() {
        // user 권한
        Set<PermissionAction> userPermissions = new HashSet<>();

        userPermissions.add(new PermissionAction("read_user", env -> {
            Long headerUserId = Long.valueOf(env.getGraphQlContext().get("X-USER-ID"));
            Long argumentUserId = Long.valueOf(env.getArgument("userId"));

            if (!headerUserId.equals(argumentUserId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        userPermissions.add(new PermissionAction("update_user", env -> {
            Long headerUserId = Long.valueOf(env.getGraphQlContext().get("X-USER-ID"));
            Long argumentUserId = Long.valueOf(env.getArgument("userId"));

            if (!headerUserId.equals(argumentUserId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        userPermissions.add(new PermissionAction("read_purchase", env -> {
            Long headerUserId = Long.valueOf(env.getGraphQlContext().get("X-USER-ID"));
            Long argumentUserId = Long.valueOf(env.getArgument("userId"));
            Long argumentCourseId = Long.valueOf(env.getArgument("courseId"));

            if (!headerUserId.equals(argumentUserId)) {
                throw new UnauthorizedException("Unauthorized");
            }

            if (!enrollmentService.checkCourseAccess(argumentCourseId, argumentUserId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        userPermissions.add(new PermissionAction("read_files", env -> {
            Long userId = Long.parseLong(env.getGraphQlContext().get("X-USER-ID"));
            CourseSession session = env.getSource();
            Long courseId = session.getCourseId();

            if (!enrollmentService.checkCourseAccess(courseId, userId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        userPermissions.add(new PermissionAction("read_enrollment", env -> {
            Long headerUserId = Long.valueOf(env.getGraphQlContext().get("X-USER-ID"));
            Long argumentUserId = Long.valueOf(env.getArgument("userId"));

            if (!headerUserId.equals(argumentUserId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        userPermissions.add(new PermissionAction("update_course", env -> {
            String headerUserRole = env.getGraphQlContext().get("X-USER-ROLE");

            if (!headerUserRole.equals("ADMIN")) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        userPermissions.add(new PermissionAction("update_record", env -> {
            Long headerUserId = Long.valueOf(env.getGraphQlContext().get("X-USER-ID"));
            Long argumentUserId = Long.valueOf(env.getArgument("userId"));

            if (!headerUserId.equals(argumentUserId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        userPermissions.add(new PermissionAction("update_purchase", env -> {
            Long headerUserId = Long.valueOf(env.getGraphQlContext().get("X-USER-ID"));
            Long argumentUserId = Long.valueOf(env.getArgument("userId"));

            if (!headerUserId.equals(argumentUserId)) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        rolePermissions.put("user", userPermissions);

        // admin 권한
        Set<PermissionAction> adminPermissions = new HashSet<>();

        adminPermissions.add(new PermissionAction("create_course", env -> {
            String headerUserRole = env.getGraphQlContext().get("X-USER-ROLE");

            if (!headerUserRole.equals("ADMIN")) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        adminPermissions.add(new PermissionAction("update_course", env -> {
            String headerUserRole = env.getGraphQlContext().get("X-USER-ROLE");

            if (!headerUserRole.equals("ADMIN")) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        adminPermissions.add(new PermissionAction("update_user", env -> {
            String headerUserRole = env.getGraphQlContext().get("X-USER-ROLE");

            if (!headerUserRole.equals("ADMIN")) {
                throw new UnauthorizedException("Unauthorized");
            }
        }));

        rolePermissions.put("admin", adminPermissions);
    }

    public boolean checkPermission(String role, String permission, DataFetchingEnvironment env) {
        log.info("checkPermission: {}, {}", role, permission);

        Set<PermissionAction> actions = rolePermissions.get(role);
        if (actions != null) {
            for (PermissionAction action : actions) {
                if (action.getPermission() != null && action.getPermission().equals(permission)) {
                    action.executeAction(env);
                    return true;
                }

            }
        }
        return false;
    }
}
