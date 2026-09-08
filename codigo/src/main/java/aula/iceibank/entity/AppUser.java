package aula.iceibank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "app_users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private Long accountNumber;
    private String role;

    protected AppUser() {
    }

    public AppUser(String username, String password, Long accountNumber, String role) {
        this.username = username;
        this.password = password;
        this.accountNumber = accountNumber;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public String getRole() {
        return role;
    }
}
