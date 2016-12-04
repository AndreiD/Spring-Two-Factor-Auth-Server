package springtemplate.services;


import springtemplate.domain.Post;

import java.util.List;

public interface PostService {

    List<Post> getLast20Posts();

    Post getPostById(int id);

    void deletePost(int id);

    Post saveOrUpdatePost(Post customer);


}
