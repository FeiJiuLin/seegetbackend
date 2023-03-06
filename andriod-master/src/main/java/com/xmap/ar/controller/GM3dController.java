package com.xmap.ar.controller;

import com.xmap.ar.entity.Collection;
import com.xmap.ar.entity.GM3d;
import com.xmap.ar.entity.Recycle;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.CollectionService;
import com.xmap.ar.service.GM3dService;
import com.xmap.ar.service.RecycleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/gm3d")
public class GM3dController {
    Logger logger = LoggerFactory.getLogger(GM3dController.class);

    @Autowired
    private GM3dService gm3dService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<GM3d> addGM3d(@RequestBody @Validated GM3d gm3d) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        gm3d.setAuthor(user.getId());
        logger.info(gm3d.toString());
        GM3d newGM3d = gm3dService.addGM3d(gm3d);
        return new ResponseEntity<>(newGM3d, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteGM3d(@PathVariable("id") int id) throws Exception{
        GM3d gm3d = gm3dService.getGM3dById(id);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setDelete(true);
        gm3dService.updateGM3d(gm3d, gm3d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(gm3d.getUid());
        recycle.setType(9);
        recycle.setAuthor(gm3d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteGM3dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        GM3d gm3d = gm3dService.getGM3dByUuid(uuid);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setDelete(true);
        gm3dService.updateGM3d(gm3d, gm3d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(gm3d.getUid());
        recycle.setType(9);
        recycle.setAuthor(gm3d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<GM3d> getGM3dById(@PathVariable("id") int id) throws Exception{
        GM3d gm3d = gm3dService.getGM3dById(id);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        return new ResponseEntity<>(gm3d, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<GM3d> getGM3dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        GM3d gm3d = gm3dService.getGM3dByUuid(uuid);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        return new ResponseEntity<>(gm3d, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<GM3d>> getGM3dByUser() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GM3d> gm3ds = gm3dService.getGM3dByAuthor(user.getId());
        return new ResponseEntity<>(gm3ds, HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<GM3d> update(@PathVariable("uuid") String uuid, @RequestBody GM3d entity) throws Exception {
        GM3d old = gm3dService.getGM3dByUuid(uuid);
        if (old == null)
            throw new NotFoundException("marker not found!");
        entity.setUid(uuid);
        GM3d res = gm3dService.updateGM3d(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<GM3d> setTitleByUuid (@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        GM3d gm3d = gm3dService.getGM3dByUuid(uuid);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setTitle(title);
        GM3d res = gm3dService.updateGM3d(gm3d, gm3d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<GM3d> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        GM3d gm3d = gm3dService.getGM3dByUuid(uuid);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setThumb(cover);
        GM3d res = gm3dService.updateGM3d(gm3d, gm3d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<GM3d> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        GM3d gm3d = gm3dService.getGM3dByUuid(uuid);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setAddress(address);
        GM3d res = gm3dService.updateGM3d(gm3d, gm3d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<GM3d> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        GM3d marker = gm3dService.getGM3dByUuid(uuid);
        if (marker == null)
            throw new NotFoundException("obj not found!");
        marker.setFolder(folderUid);
        GM3d res = gm3dService.updateGM3d(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        GM3d entity = gm3dService.getGM3dByUuid(uuid);
        if (entity == null)
            throw new NotFoundException("Folder not found!");
        // entity.setCollect(collect);
        gm3dService.updateGM3d(entity, entity.getId());
        if (collect) {
            Collection collection = new Collection();
            collection.setUid(entity.getUid());
            collection.setType(8);
            collection.setAuthor(entity.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected", HttpStatus.OK);
        } else {
            collectionService.deleteObj(entity.getUid(), entity.getAuthor());
            return new ResponseEntity<>("dismiss collection", HttpStatus.OK);
        }
    }

    @PatchMapping("{id}/setTitle")
    public ResponseEntity<GM3d> setTitle (@PathVariable("id") Integer id, @RequestParam String title) throws Exception {
        GM3d gm3d = gm3dService.getGM3dById(id);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setTitle(title);
        GM3d res = gm3dService.updateGM3d(gm3d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<GM3d> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        GM3d gm3d = gm3dService.getGM3dById(id);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setThumb(cover);
        GM3d res = gm3dService.updateGM3d(gm3d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<GM3d> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        GM3d gm3d = gm3dService.getGM3dById(id);
        if (gm3d == null)
            throw new NotFoundException("GM3d not found!");
        gm3d.setAddress(address);
        GM3d res = gm3dService.updateGM3d(gm3d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}

