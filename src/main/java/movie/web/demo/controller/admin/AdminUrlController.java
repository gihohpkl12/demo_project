package movie.web.demo.controller.admin;

import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.role.Role;
import movie.web.demo.domain.url.Url;
import movie.web.demo.event.UrlUpdateEvent;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.RoleException;
import movie.web.demo.exception.UrlException;
import movie.web.demo.form.UrlAddForm;
import movie.web.demo.form.UrlDeleteForm;
import movie.web.demo.form.UrlUpdateForm;
import movie.web.demo.service.role.RoleService;
import movie.web.demo.service.url.UrlService;
import movie.web.demo.util.BindingResultUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/url")
@RequiredArgsConstructor
public class AdminUrlController {

    private final UrlService defaultUrlService;

    private final RoleService defaultRoleService;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String URL_PAGE = "/admin/url-manage-main";
    private final String REDIRECT_URL_MANAGE = "redirect:/admin/url";

    private final String URL_UPDATE_PAGE = "/admin/url-update";

    private final String URL_ADD_PAGE = "/admin/url-add";

    private final String REDIRECT_URL_MANAGE_PAGE = "redirect:/admin/url";

    /**
     * =================================================================================================
     * Url 관리 메인 페이지
     */
    @GetMapping
    public String urlManagePage(Model model) {
        List<Url> urls = defaultUrlService.getAllUrl();
        model.addAttribute("urls", urls);
        return URL_PAGE;
    }

    /**
     * =================================================================================================
     * Url 업데이트
     */
    @GetMapping("/url-update")
    public String urlUpdatePage(@RequestParam("id") Long id, Model model) {
        try {
            List<Role> roles = defaultRoleService.getAllRole();
            Url url = defaultUrlService.findUrlById(id);
            model.addAttribute("roles", roles);
            model.addAttribute("url", url);

            return URL_UPDATE_PAGE;
        } catch (UrlException e) {
            model.addAttribute("close_message", e.getMessage());
            return URL_UPDATE_PAGE;
        }
    }

    @PostMapping("/url-update")
    @ResponseBody
    public String urlUpdate(@Validated @RequestBody UrlUpdateForm urlUpdateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BindingResultUtil.getError(bindingResult);
        }

        try {
            defaultUrlService.updateUrl(urlUpdateForm, defaultRoleService);
            return "수정 됐습니다";
        } catch (AccountException e) {
            return e.getMessage();
        } catch (UrlException e) {
            return e.getMessage();
        }
    }

    /**
     * =================================================================================================
     * Url 추가
     */
    @GetMapping("/url-add")
    public String urlAddPage(Model model) {
//        List<Role> roles = defaultRoleService.getAllRoleOrderNumNotNullSorted();
        List<Role> roles = defaultRoleService.getAllRole();
        model.addAttribute("roles", roles);
        return URL_ADD_PAGE;
    }

    @PostMapping("/url-add")
    @ResponseBody
    public String addRole(@Validated @RequestBody UrlAddForm urlAddForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BindingResultUtil.getError(bindingResult);
        }

        try {
            defaultUrlService.addUrl(urlAddForm, defaultRoleService);
            return "등록됐습니다";
        } catch (UrlException e) {
            return e.getMessage();
        } catch (RoleException e) {
            return e.getMessage();
        }
    }

    /**
     * =================================================================================================
     * Url 삭제
     */
    @PostMapping("/url-delete")
    public String deleteUrl(@Validated UrlDeleteForm urlDeleteForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_URL_MANAGE_PAGE;
        }

        try {
            defaultUrlService.deleteUrl(urlDeleteForm);
            redirectAttributes.addFlashAttribute("message", "삭제 되었습니다");
            return REDIRECT_URL_MANAGE_PAGE;
        } catch (UrlException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_URL_MANAGE_PAGE;
        }
    }

    /**
     * =================================================================================================
     * Url 변경사항 반영
     */
    @GetMapping("/url-reflect")
    public String reflectUrl() {
        applicationEventPublisher.publishEvent(new UrlUpdateEvent());
        return REDIRECT_URL_MANAGE;
    }
}
