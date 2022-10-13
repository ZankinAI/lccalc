package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ProjectManager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pmId;
    private String fio;
    private String telephone;
    private String email;
    private String post;

    @OneToMany(mappedBy = "projectManager")
    private Set<Project> projects;

    public ProjectManager() {
    }

    public ProjectManager(String fio, String telephone, String email, String post) {
        this.fio = fio;
        this.telephone = telephone;
        this.email = email;
        this.post = post;
    }

    public Long getPmId() {
        return pmId;
    }

    public void setPmId(Long pmId) {
        this.pmId = pmId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }
}
