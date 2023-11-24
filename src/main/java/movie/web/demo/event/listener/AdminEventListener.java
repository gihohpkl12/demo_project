package movie.web.demo.event.listener;

import movie.web.demo.event.RoleUpdateEvent;
import movie.web.demo.event.UrlUpdateEvent;

public interface AdminEventListener {

    public void Listen(UrlUpdateEvent urlUpdateEvent);

    public void Listen(RoleUpdateEvent roleUpdateEvent);
}
