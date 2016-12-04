package springtemplate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    private String fullName;
    private String passwordHash; //use bcrypt
    private String unique_key;

    private String google_push_notifications_token;
    private String android_info_phone_model;
    private String android_info_phone_id;
    private String last_ip;
}
