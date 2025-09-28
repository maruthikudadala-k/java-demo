// ===== Imported from: com.carbo.fleet.model.User =====
package com.carbo.fleet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Field("firstName")
    @NotEmpty(message = "first name can not be empty")
    private String firstName;

    @Field("lastName")
    @NotEmpty(message = "last name can not be empty")
    private String lastName;

    @Field("userName")
    @NotEmpty(message = "user name can not be empty")
    private String userName;

    @JsonIgnore
    @Field("password")
    @NotEmpty(message = "password can not be empty")
    private String password;

    @Field("title")
    @NotEmpty(message = "title can not be empty")
    private String title;

    @Field("authorities")
    private List<Role> authorities;

    @Field("organizationId")
    @NotEmpty(message = "organization id can not be empty")
    private String organizationId;

    @Field("districtId")
    private String districtId;

    @Field("azureId")
    private String azureId;

    @Field("signature")
    private String signature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAzureId() {
        return azureId;
    }

    public void setAzureId(String azureId) {
        this.azureId = azureId;
    }

}

// ===== Imported from: com.carbo.fleet.repository.UserMongoDbRepository =====
package com.carbo.fleet.repository;

import com.carbo.fleet.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMongoDbRepository extends MongoRepository<User, String> {
    List<User> findByOrganizationId(String organizationId);
    Optional<User> findByUserName(String userName);
}

// ===== Current file: src\main\java\com\carbo\fleet\services\UserService.java =====
package com.carbo.fleet.services;

import com.carbo.fleet.model.User;
import com.carbo.fleet.repository.UserMongoDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserMongoDbRepository userRepository;

    @Autowired
    public UserService(UserMongoDbRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> getByOrganizationId(String organizationId) {
        return userRepository.findByOrganizationId(organizationId);
    }

    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(String emailGroupId) {
        userRepository.deleteById(emailGroupId);
    }
}

