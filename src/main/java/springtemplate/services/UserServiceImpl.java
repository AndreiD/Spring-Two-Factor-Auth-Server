package springtemplate.services;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springtemplate.domain.User;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public boolean authenticate(String email, String password) {
        // Provide a sample password check: email == password
        log.info("UserServiceImpl authenticate called....");
        return Objects.equals(email, password);
    }

    @Override
    @Transactional
    public boolean register(String fullName, String email, String password, String last_ip) {

        String hashed_password = passwordEncoder.encode(password);
        String unique_key = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(hashed_password);
        user.setUnique_key(unique_key);
        user.setLast_ip(last_ip);

        if (exists(User.class, "email", email)) {
            log.warn("user with this email: " + email + " already exists.");
            return false;
        } else {
            Session session = sessionFactory.getCurrentSession();
            session.save(user);
            return true;
        }
    }


    @Override
    @Transactional
    public String getUniqueKey(String user_email) {

        Session current_session = sessionFactory.getCurrentSession();
        User user = (User) current_session.createQuery(" from User where email ='" + user_email + "' ").uniqueResult();
        String unique_key = user.getUnique_key();

        //if it was already been seen
        if (unique_key.startsWith("#")) {
            throw new RuntimeException("The unique key has already been seen. Security Error");
        }

        //update it so it can't be seen anymore
        user.setUnique_key("#" + unique_key);
        current_session.update(user);

        return unique_key;
    }

    //Checks if a key/value exists in the db.
    public boolean exists(Class aClass, String idKey, Object idValue) {
        return sessionFactory.getCurrentSession().createCriteria(aClass)
                .add(Restrictions.eq(idKey, idValue))
                .setProjection(Projections.property(idKey))
                .uniqueResult() != null;
    }
}