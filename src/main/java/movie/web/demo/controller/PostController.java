package movie.web.demo.controller;

import lombok.RequiredArgsConstructor;
import movie.web.demo.util.BindingResultUtil;
import movie.web.demo.domain.post.Post;
import movie.web.demo.domain.post.PostMetaData;
import movie.web.demo.exception.AccountException;
import movie.web.demo.exception.PostException;
import movie.web.demo.form.PostEditForm;
import movie.web.demo.form.PostDeleteForm;
import movie.web.demo.form.PostEnrollForm;
import movie.web.demo.service.image.ImageService;
import movie.web.demo.service.board.BoardService;
import movie.web.demo.service.post.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final String POST_EDIT_PAGE = "/post-edit";
    private final String REDIRECT_POST = "redirect:/post";
    private final String REDIRECT_BOARD = "redirect:/board";
    private final String POST_ADD_PAGE = "post-input";
    private final String POST_PAGE = "post";

    private final PostService defaultPostService;

    private final BoardService defaultBoardService;

    private final ImageService s3ImageService;

    private final ImageService defaultImageService;

    /**
     * =========================================================================================================
     * 게시글 작성
     */

    @GetMapping("/add-post")
    public String boardPage(@RequestParam("id") Long id, @RequestParam("boardName") String boardName, Model model) {
        model.addAttribute("boardId", id);
        model.addAttribute("boardName", boardName);
        return POST_ADD_PAGE;
    }

    @PostMapping("/add-post")
    public String newPost(@Validated  PostEnrollForm postEnrollForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_BOARD + "?id=" + postEnrollForm.getBoardId() +"&boardName="+postEnrollForm.getBoardName()+makeDefaultPaginationQuery();
        }

        try {
            defaultPostService.addPost(postEnrollForm, defaultBoardService, defaultImageService);
            redirectAttributes.addFlashAttribute("message", "작성 되었습니다");
            return REDIRECT_BOARD + "?id=" + postEnrollForm.getBoardId() +"&boardName="+postEnrollForm.getBoardName()+makeDefaultPaginationQuery();
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_BOARD + "?id=" + postEnrollForm.getBoardId() +"&boardName="+postEnrollForm.getBoardName()+makeDefaultPaginationQuery();
        } catch (PostException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_BOARD + "?id=" + postEnrollForm.getBoardId() +"&boardName="+postEnrollForm.getBoardName()+makeDefaultPaginationQuery();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_BOARD + "?id=" + postEnrollForm.getBoardId() +"&boardName="+postEnrollForm.getBoardName()+makeDefaultPaginationQuery();
        }
    }

    /**
     * =========================================================================================================
     * 게시글 수정
     */
    @PostMapping("/post-edit")
    public String editPost(@Validated PostEditForm postEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_BOARD + "?id=" + postEditForm.getBoardId() +"&boardName="+postEditForm.getBoardName()+postEditForm.getBoardName()+makeDefaultPaginationQuery();
        }

        try {
            defaultPostService.updatePost(postEditForm);
            redirectAttributes.addFlashAttribute("message", "수정 되었습니다");
            return REDIRECT_POST+"?id="+postEditForm.getPostMetaDataId() + "&boardId="+postEditForm.getBoardId() +"&boardName="+postEditForm.getBoardName()+makeDefaultPaginationQuery();
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_BOARD + "?id=" + postEditForm.getBoardId() +"&boardName="+postEditForm.getBoardName()+postEditForm.getBoardName()+makeDefaultPaginationQuery();
        } catch (PostException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_BOARD + "?id=" + postEditForm.getBoardId() +"&boardName="+postEditForm.getBoardName()+postEditForm.getBoardName()+makeDefaultPaginationQuery();
        }
    }

    @GetMapping("/post-edit")
    public String editPostPage(Model model, @RequestParam Long id, @RequestParam String boardName, @RequestParam Long boardId, @RequestParam Long accountId, @RequestParam int page, @RequestParam int size) {
        if (!defaultPostService.userCheck(accountId)) {
            return REDIRECT_BOARD +"?id="+id.toString()+"&boardName="+boardName+makeDefaultPaginationQuery();
        }

        PostMetaData postMetaData = defaultPostService.fidnPostMetaDataById(id);
        model.addAttribute("boards", defaultBoardService.getAllBoard());
        model.addAttribute("subject", postMetaData.getSubject());
        model.addAttribute("content", postMetaData.getPost().getPostContent());
        model.addAttribute("boardId", boardId);
        model.addAttribute("boardName", boardName);
        model.addAttribute("postInfo", postMetaData);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return POST_EDIT_PAGE;
    }

    /**
     * =========================================================================================================
     * 게시글 삭제
     */
    @PostMapping("/post-delete")
    public String deletePost(@Validated PostDeleteForm postDeleteForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", BindingResultUtil.getError(bindingResult));
            return REDIRECT_BOARD + "?id=" + postDeleteForm.getBoardId() +"&boardName="+postDeleteForm.getBoardName()+postDeleteForm.getBoardName()+makeDefaultPaginationQuery();
        }

        try {
            defaultPostService.deletePost(postDeleteForm, defaultImageService, s3ImageService);
            redirectAttributes.addFlashAttribute("message", "삭제되었습니다");
            return REDIRECT_BOARD + "?id=" + postDeleteForm.getBoardId() +"&boardName="+postDeleteForm.getBoardName()+makeDefaultPaginationQuery();
        } catch (AccountException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_BOARD + "?id=" + postDeleteForm.getBoardId() +"&boardName="+postDeleteForm.getBoardName()+makeDefaultPaginationQuery();
        } catch (PostException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return REDIRECT_BOARD + "?id=" + postDeleteForm.getBoardId() +"&boardName="+postDeleteForm.getBoardName()+makeDefaultPaginationQuery();
        }
    }

    /**
     * =========================================================================================================
     * 게시글 조회
     */
    @GetMapping("/post")
    public String getPost(@RequestParam("id") Long id, @RequestParam("boardName") String boardName, @RequestParam("boardId")Long boardId, @RequestParam("page") int page, @RequestParam("size") int size, Model model) {
        PostMetaData postMetaData = defaultPostService.fidnPostMetaDataById(id);
        Post post = defaultPostService.findPostByMetaDataWithReviews(postMetaData);

        model.addAttribute("post", post);
        model.addAttribute("boardName", boardName);
        model.addAttribute("boardId", boardId);
        model.addAttribute("postInfo", postMetaData);
        model.addAttribute("reviews", post.getReviews());
        model.addAttribute("boards", defaultBoardService.getAllBoard());
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return POST_PAGE;
    }

    /**
     * =========================================================================================================
     * 게시글 사진 입력
     */
    @PostMapping("/image-input")
    @ResponseBody
    public Map<String, Object> multiplePhotoUpload(MultipartRequest request) {
        Map<String, Object> responseData = new HashMap<>();
        try {
            String s3Url = s3ImageService.imageUpload(request);

            responseData.put("uploaded", true);
            responseData.put("url", s3Url);

            return responseData;
        } catch (Exception e) {
            responseData.put("uploaded", false);
            return responseData;
        }
    }

    /**
     * =========================================================================================================
     * pagination 쿼리 생성
     */
    private String makeDefaultPaginationQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("&size=10&page=0&sort=createDate,desc");
        return sb.toString();
    }

    private String makePaginationQuery(int page, int size) {
        StringBuilder sb = new StringBuilder();
        sb.append("&size=")
                .append(Integer.toString(size))
                .append("&page=")
                .append(Integer.toString(page))
                .append("&sort=createDate,desc");
        return sb.toString();
    }
}
