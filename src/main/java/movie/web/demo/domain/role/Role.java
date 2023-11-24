package movie.web.demo.domain.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
//@Table(name = "Role_Manage")
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private String role;

    private Long orderNum;
}
