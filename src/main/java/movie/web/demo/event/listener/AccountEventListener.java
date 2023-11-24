package movie.web.demo.event.listener;

import movie.web.demo.event.AccountDeleteEvent;
import movie.web.demo.event.AccountNicknameUpdateEvent;

public interface AccountEventListener {

    public void listen(AccountDeleteEvent accountDeleteEvent);

    public void listen(AccountNicknameUpdateEvent accountNicknameUpdateEvent);
}
