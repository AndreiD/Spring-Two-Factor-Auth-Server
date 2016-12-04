package springtemplate.services;

public interface UserService {
    boolean authenticate(String email, String password);
    boolean register(String fullName, String email, String password, String last_ip);
    String getUniqueKey(String user_email);
}
