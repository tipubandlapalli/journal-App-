package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getTenUsers(){
        return new ResponseEntity<>(userService.getTenUsers(),HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getByUsername(
            @PathVariable String username
    ){
        if(!userService.exitsByUsername(username)) {
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.getUserByUsername(username),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestBody UserEntity userEntity
    ){
        if(userEntity.getUsername().trim().isEmpty() || userEntity.getPassword().trim().isEmpty()){
            return new ResponseEntity<>("Invalid Username or password", HttpStatus.BAD_REQUEST);
        }
        if(userEntity.getId() != null) {
            return new ResponseEntity<>("Client can't decide ID", HttpStatus.BAD_REQUEST);
        }
        if(userService.exitsByUsername(userEntity.getUsername())){
            return new ResponseEntity<>("Username not available", HttpStatus.NOT_ACCEPTABLE);
        }
        userEntity.setLocalDateTime(LocalDateTime.now());

        return new ResponseEntity<>(userService.saveUser(userEntity),HttpStatus.CREATED);
    }

    @PutMapping("{username}")
    public ResponseEntity<?> editUserByUsername(
            @PathVariable String username,
            @RequestBody UserEntity userEntity
    ){
        if(!userService.exitsByUsername(username)){
            return new ResponseEntity<>("User does not exists",HttpStatus.NOT_FOUND);
        }

        if( userEntity.getId() != null ) {
            return new ResponseEntity<>("Can't change ID",HttpStatus.BAD_REQUEST);
        }

        if(     !userEntity.getUsername().trim().isEmpty()
                && !userEntity.getUsername().equals(username)
                && userService.exitsByUsername(userEntity.getUsername())
        ){
            return new ResponseEntity<>("Username not available", HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>(userService.editByUsername(username, userEntity), HttpStatus.OK);
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> deleteByUsername(@PathVariable String username){
        if(!userService.exitsByUsername(username)) {
            return new ResponseEntity<>("User not found",HttpStatus.NOT_FOUND);
        }
        userService.deleteUserByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
