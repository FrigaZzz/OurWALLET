package com.mfr.taass.spring.auth.api.utils;

import com.mfr.taass.spring.auth.api.entities.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class GoogleUser {

    private String id;
    private String provider;
    private String email;
    private String name;
    
    private String photoUrl;
    private String firstName;
    private String lastName;
    private String idToken;
    private String username;
    private String authorizationCode;

    public GoogleUser() {
    }

    public GoogleUser(String id, String provider, String email, String name, String photoUrl, String firstName, String lastName, String idToken, String username, String authorizationCode) {
        this.id = id;
        this.provider = provider;
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idToken = idToken;
        this.username = username;
        this.authorizationCode = authorizationCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
   
   
    
}
