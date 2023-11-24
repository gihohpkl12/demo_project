package movie.web.demo.service.url;

import movie.web.demo.domain.role.Role;
import movie.web.demo.domain.url.Url;
import movie.web.demo.form.UrlAddForm;
import movie.web.demo.form.UrlDeleteForm;
import movie.web.demo.form.UrlUpdateForm;
import movie.web.demo.service.role.RoleService;

import java.util.List;

public interface UrlService {

    List<Url> getPassAuthenticationUrls();

    List<Url> getAuthenticationUrls();

    List<Url> getSignInUrl();

    List<Url> getAllUrl();

    void deleteUrl(UrlDeleteForm urlDeleteForm);

    public void addUrl(UrlAddForm urlAddForm, RoleService roleService);

    public Url findUrlById(Long id);

    public void updateUrl(UrlUpdateForm urlUpdateForm, RoleService roleService);

    public List<Url> findUrlByRole(Role role);
}
