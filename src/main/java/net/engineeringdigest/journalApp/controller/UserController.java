package net.engineeringdigest.journalApp.controller;
import jdk.internal.org.objectweb.asm.Opcodes;
import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


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
    public ResponseEntity<?> getByUsername(@PathVariable String username){
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
        userService.saveUser(userEntity);
        return new ResponseEntity<>(userEntity,HttpStatus.CREATED);
    }
    @PutMapping("{username}")
    public ResponseEntity<?> editUserById(
            @PathVariable String username,
            @RequestBody UserEntity userEntity
    ){

        if( userEntity.getId() != null ) {
            return new ResponseEntity<>("Can't change ID",HttpStatus.UNAUTHORIZED);
        }
        if(!userService.exitsByUsername(username)){
            return new ResponseEntity<>("User does not exists",HttpStatus.NOT_FOUND);
        }
        if(     !userEntity.getUsername().trim().isEmpty()
                && !username.equals(userEntity.getUsername())
                && userService.exitsByUsername(userEntity.getUsername())
        ){
            return new ResponseEntity<>("Username not available", HttpStatus.NOT_ACCEPTABLE);
        }

        if(!userService.editByUsername(username,userEntity)) {
            return new ResponseEntity<>("You are not authorized",HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
