package com.mfr.taass.spring.auth.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfr.taass.spring.auth.api.beans.BaseResponse;
import com.mfr.taass.spring.auth.api.beans.Credentials;
import com.mfr.taass.spring.auth.api.beans.UserInput;
import com.mfr.taass.spring.auth.api.beans.StatusMeta;
import com.mfr.taass.spring.auth.api.entities.Account;
import com.mfr.taass.spring.auth.api.entities.Groups;
import com.mfr.taass.spring.auth.api.entities.User;
import com.mfr.taass.spring.auth.api.exceptions.CredentialsAlreadyExistsException;
import com.mfr.taass.spring.auth.api.exceptions.InvalidAuthorizationHeaderException;
import com.mfr.taass.spring.auth.api.exceptions.InvalidJWTTokenException;
import com.mfr.taass.spring.auth.api.exceptions.InvalidLoginParametersException;
import com.mfr.taass.spring.auth.api.exceptions.InvalidRegistrationParametersException;
import com.mfr.taass.spring.auth.api.exceptions.WrongCredentialsException;
import com.mfr.taass.spring.auth.api.repos.AccountRepository;
import com.mfr.taass.spring.auth.api.repos.GroupsRepository;
import com.mfr.taass.spring.auth.api.repos.UserRepository;
import com.mfr.taass.spring.auth.api.utils.GoogleUser;
import com.mfr.taass.spring.auth.api.utils.JwtTokenUtil;
import com.mfr.taass.spring.auth.api.utils.JwtUser;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.swagger.annotations.ApiOperation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;

@RestController
public class LoginController {
    
    @Value("${oauth.google.client-id}")
    private String clientID;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GroupsRepository groupsRepository;
    @Autowired
    private AccountRepository accountRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @ApiOperation(value = "Login using your email/username and password.",notes = "If the combination is correct, you will get a token JWT.")
    @PostMapping("/login")
    public BaseResponse<StatusMeta, Map<String, String>> login(@RequestBody Credentials credentials) throws JsonProcessingException, InvalidLoginParametersException, WrongCredentialsException {
        checkParams(credentials);

        User user = null;
        if (credentials.getUsername().contains("@")) {
            user = userRepo.findByEmail(credentials.getUsername());
        } else {
            user = userRepo.findByUsername(credentials.getUsername());
        }
        if (user == null) {
            throw new WrongCredentialsException();
        }

        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword())) {
            throw new WrongCredentialsException();
        }

        String jwt = jwtTokenUtil.generateToken(new JwtUser(user));
        Map<String, String> map = new HashMap<>();
        map.put("jwt", jwt);
        return new BaseResponse<>(new StatusMeta(200), map);
    }

    @ApiOperation(value = "Register using your email, username and password.",notes = "If the email and the username are not used, you will get a 200 OK.")
    @PostMapping("/users")
    public BaseResponse<StatusMeta, Object> signup(@RequestBody UserInput newUser) throws JsonProcessingException, InvalidRegistrationParametersException, CredentialsAlreadyExistsException {
        checkParams(newUser);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        User u = new User();

        Groups family = new Groups();
        family.setName("Family of " + newUser.getUsername());
        family.setIsFamilyGroup(Boolean.TRUE);

        groupsRepository.save(family);

        u.setEmail(newUser.getEmail());
        u.setPassword(newUser.getPassword());
        u.setUsername(newUser.getUsername());

        u.setPayer(Boolean.TRUE);
        u.setFamilyGroup(family);
        userRepo.save(u);

        Account a = new Account();
        a.setName("Default account of " + newUser.getUsername());
        a.setIsEasyPay(false);
        a.setUser(u);
        accountRepository.save(a);

        return new BaseResponse<>(new StatusMeta(200));
    }

    @ApiOperation(value = "Refresh your token, before it expires.",notes = "If the token is not expired, you will get a new token.")
    @GetMapping("/refresh")
    public BaseResponse<StatusMeta, Map<String, String>> refresh(@RequestHeader(value = "Authorization") String token) throws InvalidJWTTokenException, InvalidAuthorizationHeaderException {
        if (!token.startsWith("Bearer ")) {
            throw new InvalidAuthorizationHeaderException();
        }
        token = token.substring(7, token.length());
        token = jwtTokenUtil.refreshToken(token);
        if (token == null) {
            throw new InvalidJWTTokenException();
        }
        Map<String, String> map = new HashMap<>();
        map.put("jwt", token);
        return new BaseResponse<>(new StatusMeta(200), map);
    }

    /**
     * @String newOauthToken : rappresenta il token ricevuto da google Questa
     * chiamata è eseguita durante login e registrazione LOGIN GOOGLE > VERIFICA
     * DEL TOKEN > se passata allora se il token appartiene ad un utente
     * >ANGULAR/ ANDROID ricevono l'oggetto utente come risposta altrimenti >
     */
    @ApiOperation(value = "Register using a token from Google.",notes = "If the token is valid, you will register with your Google Account.")
    @PostMapping("/oAuthUsers")
    public BaseResponse<StatusMeta, Object> signUpWithOauth(@RequestBody GoogleUser newOauthToken, String username) throws JsonProcessingException, InvalidRegistrationParametersException, CredentialsAlreadyExistsException, IOException, GeneralSecurityException, InvalidJWTTokenException {
        Map<String, Object> map = new HashMap<>();
        final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier
                = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                        .setAudience(Collections.singletonList(clientID))
                        .build();

        final GoogleIdToken googleIdToken = verifier.verify(newOauthToken.getIdToken());
        
        
        if (googleIdToken == null) {
            throw new InvalidJWTTokenException();
        }
        
        final Payload payload = googleIdToken.getPayload();
        final Boolean emailVerified = payload.getEmailVerified();

      

        if (emailVerified) {
            User login = this.userRepo.findByEmail(payload.getEmail());
            // LOGIN CORRETTO
            if (login != null) {
                String jwt = jwtTokenUtil.generateToken(new JwtUser(login));
                map.put("jwt", jwt);
                
                return new BaseResponse<>(new StatusMeta(200), map);

            } else {
                // REGISTRAZIONE
                String newUsername = (String) payload.get("given_name") +"."+payload.get("family_name");
                if (username != null && username != "") {
                    newUsername = username;
                }
                User u = this.userRepo.findByUsername(newUsername);

                if (u != null) {
                    throw new CredentialsAlreadyExistsException(true, false);
                }

                u=new User();
                Groups family = new Groups();
                family.setName("Family of " + newUsername);
                family.setIsFamilyGroup(Boolean.TRUE);

                groupsRepository.save(family);

                u.setEmail(payload.getEmail());
                RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45).build();

                u.setPassword(pwdGenerator.generate(25));
                u.setUsername(newUsername);

                u.setPayer(Boolean.TRUE);
                u.setFamilyGroup(family);
                userRepo.save(u);

                Account a = new Account();
                a.setName("Default account of " + newUsername);
                a.setIsEasyPay(false);
                a.setUser(u);
                accountRepository.save(a);

                String jwt = jwtTokenUtil.generateToken(new JwtUser(u));
                map.put("jwt", jwt);
                return new BaseResponse<>(new StatusMeta(200), map);
            }
        }
        
        throw new CredentialsAlreadyExistsException(true, true);
        
    }

    private void checkParams(UserInput newUser) throws InvalidRegistrationParametersException, CredentialsAlreadyExistsException {
        boolean isValid = newUser.getEmail() != null
                && newUser.getUsername() != null
                && newUser.getPassword() != null
                && !newUser.getUsername().contains("@")
                && newUser.getEmail().contains("@");
        if (!isValid) {
            throw new InvalidRegistrationParametersException(newUser);
        }
        boolean usernameExists = userRepo.existsByUsername(newUser.getUsername());
        boolean emailExists = userRepo.existsByEmail(newUser.getEmail());
        if (usernameExists || emailExists) {
            throw new CredentialsAlreadyExistsException(usernameExists, emailExists);
        }
    }

    private void checkParams(Credentials credentials) throws InvalidLoginParametersException {
        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            throw new InvalidLoginParametersException(credentials);
        }
    }

    private GoogleIdToken verifyFromWeb(String path) throws IOException {
        URL urlForGetRequest = new URL(path);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            // print result

            String responseJson = response.toString();
            String[] myRes = responseJson.split("\\{");
            String res = "";
            for (int i = 1; i < myRes.length; i++) {
                res = res + myRes[i];
            }
            res = "{ " + res;

            return null;
            //GetAndPost.POSTRequest(response.toString());
        } else {
            System.out.println("GET NOT WORKED" + path);
            return null;
        }

    }

}
