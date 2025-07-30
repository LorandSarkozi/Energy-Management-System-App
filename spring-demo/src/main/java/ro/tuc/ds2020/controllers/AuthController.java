package ro.tuc.ds2020.controllers;


import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import ro.tuc.ds2020.constants.UserRole;
import ro.tuc.ds2020.dtos.AuthRequestDto;

import ro.tuc.ds2020.dtos.PersonDTO;
import ro.tuc.ds2020.dtos.SignupRequestDto;
import ro.tuc.ds2020.entities.Person;
import ro.tuc.ds2020.security.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ro.tuc.ds2020.repositories.PersonRepository;
import ro.tuc.ds2020.services.PersonService;

import java.io.IOException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final PersonService personService;
    private final PersonRepository personRepository;

    private final JwtUtil jwtUtil;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthRequestDto authenticationRequest, HttpServletResponse response) throws IOException, JSONException {
        System.out.println("Checked\n");
        
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch(BadCredentialsException e){
            throw new BadCredentialsException("Incorrect email or password.");
        }
        System.out.println("Passed\n");
        System.out.println(authenticationRequest.getUsername());
        final UserDetails userDetails = personService.loadUserByUsername(authenticationRequest.getUsername());
        System.out.println("userDetails\n");
        Person optionalUser = personRepository.findByAddress(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        System.out.println("jwt\n");
        if(optionalUser != null){
            response.getWriter().write(new JSONObject()
                    .put("userId",optionalUser.getId())
                    .put("role",optionalUser.getRole())
                    .toString()
            );

            response.addHeader("Access-Control-Expose-Headers","Authorization");
            response.addHeader("Access-Control-Allow-Headers","Authorization, X-PINGOTHER, Origin, " +
                    "X-Requested-With, Content-Type, Accept, X-Custom-header");
            response.addHeader(HEADER_STRING,TOKEN_PREFIX + jwt);
        }
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequestDto signupRequest){

        if(personService.hasUserWithAddress(signupRequest.getAddress())){
            return new ResponseEntity<>("User already exists.", HttpStatus.NOT_ACCEPTABLE);
        }

        PersonDTO userDto = personService.createUser(signupRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

    @PostConstruct
    public void createAdmin(){

        Person admin = personRepository.findByRole(UserRole.ADMIN);
        if(null == admin){
            Person user = new Person();
            user.setAddress("admin@gmail.com");
            user.setName("admin");
            user.setRole(UserRole.ADMIN);
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            user.setAge(40);
            personRepository.save(user);
        }
    }

}