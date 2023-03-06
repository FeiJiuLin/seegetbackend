//package com.xmap.v04.controller;
//
//import com.xmap.v04.config.security.SecurityConfig;
//import com.xmap.v04.entity.Source;
//import com.xmap.v04.entity.User;
//import com.xmap.v04.exception.BaseException;
//import com.xmap.v04.models.SourceModel;
//import com.xmap.v04.service.SourceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("api/source")
//public class SourceController {
//    Logger logger = LoggerFactory.getLogger(SourceController.class);
//    @Autowired
//    private SourceService sourceService;
//
//    @PostMapping()
//    public ResponseEntity<Source> create(@RequestBody SourceModel sourceModel) throws Exception{
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        try {
//            Source source = sourceService.addSource(sourceModel, user.getId());
//            return new ResponseEntity<>(source, HttpStatus.CREATED);
//        } catch (Exception e) {
//            logger.error(e.toString());
//            throw new BaseException(e.toString());
//        }
//    }
//
//    @GetMapping("self")
//    public ResponseEntity<List<Source>> getSelfSource() throws Exception {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        try {
//            List<Source> sources = sourceService.getSourceByCreator(user.getId());
//            return new ResponseEntity<>(sources, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error(e.toString());
//            throw new BaseException(e.toString());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteById(@PathVariable int id) throws Exception {
//        try {
//            sourceService.deleteSourceById(id);
//            return new ResponseEntity<>("deleted", HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error(e.toString());
//            throw new BaseException(e.toString());
//        }
//    }
//}
