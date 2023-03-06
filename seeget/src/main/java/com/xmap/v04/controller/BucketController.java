package com.xmap.v04.controller;

import com.xmap.v04.entity.Bucket;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.BaseException;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.service.BucketService;
import com.xmap.v04.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/bucket")
public class BucketController {
    Logger logger = LoggerFactory.getLogger(BucketController.class);

    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping()
    public ResponseEntity<Bucket> create(@RequestParam String bucketName) throws Exception{
        User creator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(bucketService.getBucketByName(bucketName) == null) {
            Bucket bucket = bucketService.addBucket(bucketName, creator.getId());
            return new ResponseEntity<>(bucket, HttpStatus.CREATED);
        } else {
            throw new BaseException("bucketName already exits.");
        }
    }

    @DeleteMapping("/{bucketName}")
    public ResponseEntity<String> delete(@PathVariable String bucketName) throws Exception {
        Bucket bucket = bucketService.getBucketByName(bucketName);
        if (bucket == null) {
            throw new NotFoundException("bucketName does not exits.");
        } else {
            bucketService.deleteBucketByName(bucketName);
            return new ResponseEntity<>("bucket delete successfully.", HttpStatus.OK);
        }
    }

    @GetMapping("self")
    public ResponseEntity<Bucket> find() throws Exception {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Bucket bucket = bucketService.getBucketByOwner(owner.getId());
        return new ResponseEntity<>(bucket, HttpStatus.OK);
    }

}
