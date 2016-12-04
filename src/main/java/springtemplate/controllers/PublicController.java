package springtemplate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import springtemplate.services.PostService;

@Controller
public class PublicController {

    private PostService postService;

    @Autowired
    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postService.getLast20Posts());
        System.out.println("called /");
        return "/public/index";
    }

    @RequestMapping("/post/{id}")
    public String getPost(@PathVariable Integer id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return "/public/post";
    }

    @RequestMapping("/about")
    public String about() {
        return "/public/about";
    }


}
