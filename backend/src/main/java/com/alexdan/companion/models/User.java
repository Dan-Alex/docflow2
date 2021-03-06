package com.alexdan.companion.models;

import com.alexdan.companion.models.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(JsonViews.Public.class)
    private long id;

    @JsonView(JsonViews.Public.class)
    private String username;

    @JsonView(JsonViews.Public.class)
    private String name;

    @JsonView(JsonViews.Public.class)
    private String surname;

    @JsonView(JsonViews.FullInfo.class)
    private String password;

    @JsonView(JsonViews.Public.class)
    private String position;

    @JsonView(JsonViews.Public.class)
    private String email;

    @JsonView(JsonViews.Public.class)
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Department department;

    @JsonView(JsonViews.Public.class)
    private String departmentName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Role> roles;

    @OneToMany(mappedBy="toWhom", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> incomingTasks;

    @OneToMany(mappedBy="fromWhom", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Task> outgoingTasks;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Document> documents;

    public void addDocument(Document document){
        this.documents.add(document);
    }

    public void addIncomingTask(Task task) { this.incomingTasks.add(task); }

    public void addOutgoingTask(Task task) { this.outgoingTasks.add(task); }




    public User(){}

    public User(String username, String name, String surname, String password, String position, String email, String phone) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.position = position;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Department getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Task> getIncomingTasks() {
        return incomingTasks;
    }

    public List<Task> getOutgoingTasks() {
        return outgoingTasks;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}