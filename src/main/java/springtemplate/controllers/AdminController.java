package springtemplate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springtemplate.domain.Post;
import springtemplate.services.NotificationService;
import springtemplate.services.PostService;


@Controller
@Slf4j
public class AdminController {

    private PostService postService;

    @Autowired
    private NotificationService notifyService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping("/admin")
    public String listAdminPosts(Model model) {
        model.addAttribute("posts", postService.getLast20Posts());
        return "/admin/admin_index";
    }


    @RequestMapping("/admin/post/delete/{id}")
    public String deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return "redirect:/admin";
    }

    @RequestMapping("/admin/post/edit/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "/admin/postForm";
    }

    @RequestMapping("/admin/post/new")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "/admin/postForm";
    }

    @RequestMapping(value = "/admin/post", method = RequestMethod.POST)
    public String saveOrUpdatePost(Post post) {

        if ((post.getTitle().length() < 1) || (post.getContent().length() < 1)) {
            notifyService.error_notification("Please write some content for this post.");
            return "redirect:/admin/post/new";
        }

        Post savedPost = postService.saveOrUpdatePost(post);
        return "redirect:/admin";

    }

}
