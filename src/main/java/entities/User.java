package entities;
import lombok.Data;

@Data
public class User {
    private Integer id;
    private String login;
    private String password;
    private String email;
    private String phone;
    private String status;
}
