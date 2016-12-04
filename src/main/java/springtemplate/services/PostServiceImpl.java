package springtemplate.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springtemplate.domain.Post;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    @Transactional
    public List<Post> getLast20Posts() {

        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Post");
        query.setFirstResult(0);
        query.setMaxResults(20);
        List<Post> postsList = (List<Post>) query.list();
        return postsList;
    }

    @Override
    @Transactional
    public Post getPostById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Post p = session.load(Post.class, new Integer(id));
        return p;
    }

    @Override
    @Transactional
    public void deletePost(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Post p = session.load(Post.class, new Integer(id));
        if (null != p) {
            session.delete(p);
        }
    }

    @Override
    @Transactional
    public Post saveOrUpdatePost(@NotNull Post post) {

        post.setPublishedDate(new Date());

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(post);
        return post;
    }
}
