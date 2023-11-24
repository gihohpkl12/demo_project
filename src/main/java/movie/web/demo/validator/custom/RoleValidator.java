package movie.web.demo.validator.custom;

import movie.web.demo.domain.account.Account;
import movie.web.demo.domain.role.Role;
import movie.web.demo.domain.url.Url;
import movie.web.demo.exception.RoleException;
import movie.web.demo.service.account.UserAccountManageService;
import movie.web.demo.service.url.UrlService;

import java.util.List;
import java.util.Optional;

public class RoleValidator extends DefaultValidator{

   public boolean updateCheck(Optional<?> t, Optional<?> t2, String role) {
       if (!updateCheck(t)) {
           throw new RoleException("존재하지 않는 권한입니다");
       }

       if (role.indexOf("ROLE_") == -1) {
           throw new RoleException("권한명을 확인해주시기 바랍니다");
       }

       if (isExist(t2)) {
           Role preRole = (Role) t.get();
           Role newRole = (Role) t2.get();
           if (!preRole.getRole().equals(newRole)) {
               throw new RoleException("이미 존재하는 권한입니다");
           }
       }

       return true;
   }

   public boolean deleteCheck(Optional<?> t, UserAccountManageService userAccountManageService, UrlService urlService) {
       if (!isExist(t)) {
           throw new RoleException("존재하지 않는 role입니다");
       }

       Optional<Role> role = (Optional<Role>) t;
       List<Account> accounts = userAccountManageService.findAccountByRole(role.get().getRole());
       if (accounts.size() != 0) {
           throw new RoleException("해당 role을 갖고 있는 계정이 존재합니다");
       }

       List<Url> urls = urlService.findUrlByRole(role.get());
       if (urls.size() != 0) {
           throw new RoleException("해당 role을 갖고 있는 url이 존재합니다");
       }

       return true;
   }

    public boolean addCheck(Optional<?> t, String role) {
        if (!addCheck(t)) {
            throw new RoleException("이미 존재하는 권한입니다");
        }

        if (role.indexOf("ROLE_") == -1) {
            System.out.println("ok ? "+role.indexOf("ROLE_"));
            throw new RoleException("권한명을 확인해주시기 바랍니다");
        }

        return true;
    }

//    private boolean isExist(Optional<?> t) {
//        return t.isPresent();
//    }
}
