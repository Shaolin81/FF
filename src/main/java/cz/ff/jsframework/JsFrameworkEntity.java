package cz.ff.jsframework;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name= "JS_FRAMEWORK", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "version" }) })
public class JsFrameworkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String version;

    @Column(nullable = false)
    private LocalDate deprecationDate;

    @Column(nullable = false)
    private Integer hypeLevel;

    JsFrameworkEntity() {};

    public JsFrameworkEntity(String name, String version, LocalDate deprecationDate, int hypeLevel) {
        this.name = name;
        this.version = version;
        this.deprecationDate = deprecationDate;
        this.hypeLevel = hypeLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDate getDeprecationDate() {
        return deprecationDate;
    }

    public void setDeprecationDate(LocalDate deprecationDate) {
        this.deprecationDate = deprecationDate;
    }

    public int getHypeLevel() {
        return hypeLevel;
    }

    public void setHypeLevel(int hypeLevel) {
        this.hypeLevel = hypeLevel;
    }
}
