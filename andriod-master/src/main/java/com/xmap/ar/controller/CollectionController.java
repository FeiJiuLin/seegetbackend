package com.xmap.ar.controller;

import com.xmap.ar.entity.*;
import com.xmap.ar.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private M2dService m2dService;

    @Autowired
    private M3dService m3dService;

    @Autowired
    private MM2dService mm2dService;

    @Autowired
    private MM3dService mm3dService;

    @Autowired
    private GM2dService gm2dService;

    @Autowired
    private GM3dService gm3dService;

    @Autowired
    private ObjService objService;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private FolderService folderService;

    @GetMapping("M2D")
    public ResponseEntity<List<M2d>> getM2d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<M2d> m2ds = new ArrayList<>();
        for(Collection collection: collections) {
            m2ds.add(m2dService.selectByUUid(collection.getUid()));
        }
        return new ResponseEntity<>(m2ds, HttpStatus.OK);
    }

    @GetMapping("M3D")
    public ResponseEntity<List<M3d>> getM3d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<M3d> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(m3dService.selectByUUid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("Obj")
    public ResponseEntity<List<Obj>> getObj() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<Obj> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(objService.selectByUUid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("Scene")
    public ResponseEntity<List<Scene>> getScene() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<Scene> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(sceneService.selectByUUid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("MM2D")
    public ResponseEntity<List<MM2d>> getMM2d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<MM2d> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(mm2dService.getMM2dByUuid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("MM3D")
    public ResponseEntity<List<MM3d>> getMM3d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<MM3d> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(mm3dService.getMM3dByUuid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("GM2D")
    public ResponseEntity<List<GM2d>> getGM2d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<GM2d> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(gm2dService.getGM2dByUuid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("GM3D")
    public ResponseEntity<List<GM3d>> getGM3d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<GM3d> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(gm3dService.getGM3dByUuid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("Folder")
    public ResponseEntity<List<Folder>> getFolder() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Collection> collections = collectionService.selectByAuthor(1, user.getId());
        List<Folder> res = new ArrayList<>();
        for(Collection collection: collections) {
            res.add(folderService.getFolderByUuid(collection.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
