package movie.web.demo.service.url;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.role.Role;
import movie.web.demo.domain.url.Url;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.RoleException;
import movie.web.demo.exception.UrlException;
import movie.web.demo.form.UrlAddForm;
import movie.web.demo.form.UrlDeleteForm;
import movie.web.demo.form.UrlUpdateForm;
import movie.web.demo.repository.UrlRepository;
import movie.web.demo.service.account.UserAccountService;
import movie.web.demo.service.role.RoleService;
import movie.web.demo.service.role.RoleType;
import movie.web.demo.validator.custom.UrlValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultUrlService implements UrlService {

    private final UrlRepository urlRepository;

    private final UrlValidator urlValidator = new UrlValidator();

    public Url findUrlById(Long id) {
        Optional<Url> url = urlRepository.findById(id);
        if (!url.isPresent()) {
            throw new UrlException("등록되지 않은 url입니다");
        }
        return url.get();
    }

    private Optional<Url> findUrlByPath(String path) {
        return urlRepository.findUrlByPath(path);
    }

    @Override
    public List<Url> getPassAuthenticationUrls() {
        return urlRepository.getAllNonAuthenticationUrl();
    }

    public List<Url> getAuthenticationUrls() {
        return urlRepository.getAllAuthenticationUrl();
    }
    public List<Url> getAllUrl() {
        return urlRepository.findAll();
    }

    public List<Url> getSignInUrl() {
        List<Url> urls = urlRepository.getAllAuthenticationUrl();
        List<Url> result = new ArrayList<>();
        for (Url url : urls) {
            if (url.getRole().getRole().equals("ROLE_ANONYMOUS")) {
                result.add(url);
            }
        }
        return result;
    }

    public void addUrl(UrlAddForm urlAddForm, RoleService roleService) {
        if(!urlValidator.addCheck(findUrlByPath(urlAddForm.getPath()))) {
            throw new UrlException("이미 등록된 url입니다");
        }

        urlRepository.save(createUrl(urlAddForm, roleService));
    }

    private Url createUrl(UrlAddForm urlAddForm, RoleService roleService) {
        Url url = new Url();
        url.setPath(urlAddForm.getPath());
        if (urlAddForm.getRole() == 0) {
            return url;
        }

        Optional<Role> role = roleService.findRoleById(urlAddForm.getRole());
        if (!role.isPresent()) {
            throw new RoleException("해당 role이 없습니다");
        }

        url.setRole(role.get());
        url.setOrderNum(urlAddForm.getOrderNum());
        return url;
    }

    public void deleteUrl(UrlDeleteForm urlDeleteForm) {
        Optional<Url> url = urlRepository.findById(urlDeleteForm.getId());
        if (!urlValidator.deleteCheck(url)) {
            throw new UrlException("존재하지 않는 url입니다");
        }

        urlRepository.delete(url.get());
    }

    @Transactional
    public void updateUrl(UrlUpdateForm urlUpdateForm, RoleService roleService) {
        Optional<Url> url = urlRepository.findById(urlUpdateForm.getId());
        Optional<Url> newUrl = urlRepository.findUrlByPath(urlUpdateForm.getPath());
        if (urlValidator.updateCheck(url, newUrl)) {
            url.get().setPath(urlUpdateForm.getPath());
            url.get().setOrderNum(urlUpdateForm.getOrderNum());

            if (urlUpdateForm.getRole() != 0) {
                Optional<Role> role = roleService.findRoleById(urlUpdateForm.getRole());
                if (!role.isPresent()) {
                    throw new RoleException("존재하지 않는 role입니다");
                }
                url.get().setRole(role.get());
            } else {
                url.get().setRole(null);
            }
        }
    }

    public List<Url> findUrlByRole(Role role) {
        return urlRepository.findUrlByRole(role);
    }
}
