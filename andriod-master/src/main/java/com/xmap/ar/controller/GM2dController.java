package com.xmap.ar.controller;

import com.xmap.ar.entity.Collection;
import com.xmap.ar.entity.GM2d;
import com.xmap.ar.entity.Recycle;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.CollectionService;
import com.xmap.ar.service.GM2dService;
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
@RequestMapping("api/gm2d")
public class GM2dController {
    Logger logger = LoggerFactory.getLogger(GM2dController.class);

    @Autowired
    private GM2dService gm2dService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<GM2d> addGM2d(@RequestBody @Validated GM2d gm2d) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        gm2d.setAuthor(user.getId());
        logger.info(gm2d.toString());
        GM2d newGM2d = gm2dService.addGM2d(gm2d);
        return new ResponseEntity<>(newGM2d, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteGM2d(@PathVariable("id") int id) throws Exception{
        GM2d gm2d = gm2dService.getGM2dById(id);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setDelete(true);
        gm2dService.updateGM2d(gm2d, gm2d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(gm2d.getUid());
        recycle.setType(8);
        recycle.setAuthor(gm2d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteGM2dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        GM2d gm2d = gm2dService.getGM2dByUuid(uuid);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setDelete(true);
        gm2dService.updateGM2d(gm2d, gm2d.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(gm2d.getUid());
        recycle.setType(8);
        recycle.setAuthor(gm2d.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<GM2d> getGM2dById(@PathVariable("id") int id) throws Exception{
        GM2d gm2d = gm2dService.getGM2dById(id);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        return new ResponseEntity<>(gm2d, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<GM2d> getGM2dByUuid(@PathVariable("uuid") String uuid) throws Exception{
        GM2d gm2d = gm2dService.getGM2dByUuid(uuid);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        return new ResponseEntity<>(gm2d, HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<GM2d>> getGM2dByUser() throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GM2d> gm2ds = gm2dService.getGM2dByAuthor(user.getId());
        return new ResponseEntity<>(gm2ds, HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<GM2d> update(@PathVariable("uuid") String uuid, @RequestBody GM2d entity) throws Exception {
        GM2d old = gm2dService.getGM2dByUuid(uuid);
        if (old == null)
            throw new NotFoundException("marker not found!");
        entity.setUid(uuid);
        GM2d res = gm2dService.updateGM2d(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<GM2d> setTitleByUuid (@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        GM2d gm2d = gm2dService.getGM2dByUuid(uuid);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setTitle(title);
        GM2d res = gm2dService.updateGM2d(gm2d, gm2d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<GM2d> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        GM2d gm2d = gm2dService.getGM2dByUuid(uuid);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setThumb(cover);
        GM2d res = gm2dService.updateGM2d(gm2d, gm2d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<GM2d> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        GM2d gm2d = gm2dService.getGM2dByUuid(uuid);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setAddress(address);
        GM2d res = gm2dService.updateGM2d(gm2d, gm2d.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<GM2d> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        GM2d marker = gm2dService.getGM2dByUuid(uuid);
        if (marker == null)
            throw new NotFoundException("obj not found!");
        marker.setFolder(folderUid);
        GM2d res = gm2dService.updateGM2d(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        GM2d entity = gm2dService.getGM2dByUuid(uuid);
        if (entity == null)
            throw new NotFoundException("Folder not found!");
        // entity.setCollect(collect);
        gm2dService.updateGM2d(entity, entity.getId());
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
    public ResponseEntity<GM2d> setTitle (@PathVariable("id") Integer id, @RequestParam String title) throws Exception {
        GM2d gm2d = gm2dService.getGM2dById(id);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setTitle(title);
        GM2d res = gm2dService.updateGM2d(gm2d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<GM2d> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        GM2d gm2d = gm2dService.getGM2dById(id);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setThumb(cover);
        GM2d res = gm2dService.updateGM2d(gm2d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<GM2d> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        GM2d gm2d = gm2dService.getGM2dById(id);
        if (gm2d == null)
            throw new NotFoundException("GM2d not found!");
        gm2d.setAddress(address);
        GM2d res = gm2dService.updateGM2d(gm2d, id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}