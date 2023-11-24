package movie.web.demo.domain.url;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import movie.web.demo.domain.role.Role;

@Entity
@Setter
@Getter
public class Url {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @JoinColumn(nullable = true)
    private Long orderNum = 0l;

    public Url() {}

    public Url(String path, Role role) {
        this.path = path;
        this.role = role;
    }

}
