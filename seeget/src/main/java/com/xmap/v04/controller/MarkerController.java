package com.xmap.v04.controller;

import com.xmap.v04.entity.Marker;
import com.xmap.v04.entity.User;
import com.xmap.v04.models.MarkerModel;
import com.xmap.v04.models.MarkerResponse;
import com.xmap.v04.service.BucketService;
import com.xmap.v04.service.MarkerService;
import com.xmap.v04.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/marker")
public class MarkerController {
    Logger logger = LoggerFactory.getLogger(MarkerController.class);

    @Autowired
    private MarkerService markerService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<MarkerResponse> create(@RequestBody MarkerModel makerModel) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Marker marker = markerService.addMaker(makerModel, user.getId());
        return new ResponseEntity<>(transformOne(marker), HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<MarkerResponse>> list() {
        List<Marker> markerList = markerService.selectALL();
        return new ResponseEntity<>(transformList(markerList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarkerResponse> retrieve(@PathVariable int id) throws Exception {
        Marker marker = markerService.selectById(id);
        return new ResponseEntity<>(transformOne(marker),HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<MarkerResponse>> listSelf() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Marker> markerList = markerService.selectByCreator(user.getId());
        return new ResponseEntity<>(transformList(markerList), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) throws Exception {
        try{
            markerService.delete(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    private List<MarkerResponse> transformList(List<Marker> markerList) {
        List<MarkerResponse> res = new ArrayList<>();
        for(Marker marker : markerList) {
            res.add(transformOne(marker));
        }
        return res;
    }

    public MarkerResponse transformOne(Marker marker) {
        MarkerResponse res = new MarkerResponse();
        res.setId(marker.getId());
        res.setCreator(userService.findUserById(marker.getCreator()));
        res.setBucket(bucketService.getBucketById(marker.getBucketId()));
        res.setTitle(marker.getTitle());
        res.setDescription(marker.getDescription());
        res.setCreateTime(marker.getCreateTime());
        res.setUpdateTime(marker.getUpdateTime());
        res.setKey(marker.getKey());
        res.setHeight(marker.getHeight());
        res.setShare(marker.getShare());
        res.setWidth(marker.getWidth());
        return res;
    }
}
