package org.catolica.prog4.persistencia.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "User.authenticate",
            query = "SELECT o FROM User o WHERE o.email = :email AND o.senha = :senha")
    ,
    @NamedQuery(name = "User.findby.name",
            query = "SELECT o FROM User o WHERE o.nome = :nome")
    ,
    @NamedQuery(name = "User.findby.email",
            query = "SELECT o FROM User o WHERE o.email = :email")
    ,
    @NamedQuery(name = "User.findby.keyword",
            query = "SELECT o FROM User o WHERE o.nome LIKE :keyword ORDER BY o.nome")
    ,
    @NamedQuery(name = "User.findall.orderby.name",
            query = "SELECT o FROM User o ORDER BY o.nome")
})

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 45)
    private String nome;

    @Column(unique = true, nullable = false, length = 45)
    private String email;

    @Column(unique = true, nullable = false, length = 45)
    private String senha;

    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;

    public User() {
        super();
    }

    public User(String nome, String email, String senha, Rule rule) {
        this();
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        setRule(rule);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "User[ id=" + id + " ] [ nome = " + this.getNome() + " ]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

}
