package movie.web.demo.repository;

import movie.web.demo.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.orderNum is not null ORDER BY r.orderNum ASC")
    List<Role> getAllRoleSortedByOrder();

    @Query("select r from Role r")
    List<Role> getAllRole();

    @Query("select r from Role r where r.role = :role")
    Optional<Role> getRoleByRoleName(@Param("role") String role);
}
