package movie.web.demo.service.role;

public enum RoleType {

    ADMIN("ROLE_ADMIN", 3), MANAGER("ROLE_MANAGER", 2), USER("ROLE_USER", 1);

    private String role;

    private int order;

    RoleType(String role, int order) {
        this.role = role;
        this.order = order;
    }

    public boolean isGranted(String curRole, String requiredRole) {
        int curRoleOrder = -1;
        int requiredRoleOrder = -1;

        for (RoleType roleType : RoleType.values()) {
            if (roleType.role.equals(curRole)) {
                curRoleOrder = roleType.order;
            }

            if (roleType.role.equals(requiredRole)) {
                requiredRoleOrder = roleType.order;
            }
        }

        if (curRoleOrder == -1 || requiredRoleOrder == -1) {
            return false;
        }

        if (curRoleOrder >= requiredRoleOrder) {
            return true;
        }

        return false;
    }

}
