package com.xmap.ar.controller;

import com.xmap.ar.entity.*;
import com.xmap.ar.service.*;
import org.checkerframework.checker.units.qual.A;
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
@RequestMapping("api/recycle")
public class RecycleController {
    @Autowired
    private RecycleService recycleService;

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

    @GetMapping("deleteM2D")
    public ResponseEntity<String> deleteM2D() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteM2d(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("M2D")
    public ResponseEntity<List<M2d>> getM2d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(1, user.getId());
        List<M2d> m2ds = new ArrayList<>();
        for(Recycle recycle: recycles) {
            m2ds.add(m2dService.selectByUUid(recycle.getUid()));
        }
        return new ResponseEntity<>(m2ds, HttpStatus.OK);
    }

    @GetMapping("deleteM3D")
    public ResponseEntity<String> deleteM3D() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteM3d(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("M3D")
    public ResponseEntity<List<M3d>> getM3d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(2, user.getId());
        List<M3d> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(m3dService.selectByUUid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("deleteObj")
    public ResponseEntity<String> deleteObj() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteObj(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("Obj")
    public ResponseEntity<List<Obj>> getObj() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(3, user.getId());
        List<Obj> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(objService.selectByUUid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("deleteScene")
    public ResponseEntity<String> deleteScene() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteScene(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("Scene")
    public ResponseEntity<List<Scene>> getScene() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(4, user.getId());
        List<Scene> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(sceneService.selectByUUid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("deleteFolder")
    public ResponseEntity<String> deleteFolder() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteFolder(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("Folder")
    public ResponseEntity<List<Folder>> getFolder() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(5, user.getId());
        List<Folder> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(folderService.getFolderByUuid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("deleteMM2D")
    public ResponseEntity<String> deleteMM2D() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteMM2D(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("MM2D")
    public ResponseEntity<List<MM2d>> getMM2d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(6, user.getId());
        List<MM2d> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(mm2dService.getMM2dByUuid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("deleteMM3D")
    public ResponseEntity<String> deleteMM3D() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteMM3D(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("MM3D")
    public ResponseEntity<List<MM3d>> getMM3d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(7, user.getId());
        List<MM3d> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(mm3dService.getMM3dByUuid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("deleteGM2D")
    public ResponseEntity<String> deleteGM2D() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteGM2D(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("GM2D")
    public ResponseEntity<List<GM2d>> getGM2d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(8, user.getId());
        List<GM2d> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(gm2dService.getGM2dByUuid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("deleteGM3D")
    public ResponseEntity<String> deleteGM3D() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        recycleService.deleteGM3D(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

    @GetMapping("GM3D")
    public ResponseEntity<List<GM3d>> getGM3d() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        List<Recycle> recycles = recycleService.selectByAuthor(9, user.getId());
        List<GM3d> res = new ArrayList<>();
        for(Recycle recycle: recycles) {
            res.add(gm3dService.getGM3dByUuid(recycle.getUid()));
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
