package com.admedia.bendre.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "token",
        "user_email",
        "user_nicename",
        "user_display_name"
})
public class AppUser implements Serializable {
    private int id;
    @JsonProperty("token")
    private String token;
    @JsonProperty("user_email")
    private String userEmail;
    @JsonProperty("user_nicename")
    private String userNicename;
    @JsonProperty("user_display_name")
    private String userDisplayName;
    private boolean enableNotifications;
    private String password;
    private String firstName;
    private String lastName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    public AppUser(int id, String token, String userEmail, String userNicename, String userDisplayName, String password, String firstName, String lastName) {
        this.id = id;
        this.token = token;
        this.userEmail = userEmail;
        this.userDisplayName = userDisplayName;
        this.userNicename = userNicename;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public AppUser(String token, String userEmail, String userNicename, String userDisplayName, String password, String firstName, String lastName) {
        this.token = token;
        this.userEmail = userEmail;
        this.userDisplayName = userDisplayName;
        this.userNicename = userNicename;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("user_email")
    public String getUserEmail() {
        return userEmail;
    }

    @JsonProperty("user_email")
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @JsonProperty("user_nicename")
    public String getUserNicename() {
        return userNicename;
    }

    @JsonProperty("user_nicename")
    public void setUserNicename(String userNicename) {
        this.userNicename = userNicename;
    }

    @JsonProperty("user_display_name")
    public String getUserDisplayName() {
        return userDisplayName;
    }

    @JsonProperty("user_display_name")
    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnableNotifications() {
        return enableNotifications;
    }

    public void setEnableNotifications(boolean enableNotifications) {
        this.enableNotifications = enableNotifications;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}