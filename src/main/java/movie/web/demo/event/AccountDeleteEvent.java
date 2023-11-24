package movie.web.demo.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccountDeleteEvent {

    private Long id;

    private String nickname;

    public AccountDeleteEvent(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
