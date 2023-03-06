package com.xmap.ar.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.*;
import com.xmap.ar.exception.BaseException;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/obj")
public class ObjController {
    Logger logger = LoggerFactory.getLogger(ObjController.class);

    @Autowired
    private ObjService objService;

    @Autowired
    private BucketService bucketService;

    @Autowired
    private UserService userService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<Obj> create(@RequestBody Obj obj) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Obj newObj = objService.addObj(obj, user.getId());
        return new ResponseEntity<>(newObj, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Obj>> getAll() {
        List<Obj> objs = objService.selectALL();
        return new ResponseEntity<>(objs, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Obj> getOne(@PathVariable int id) throws Exception {
        Obj obj = objService.selectById(id);
        return new ResponseEntity<>(obj,HttpStatus.OK);
    }

    @GetMapping("v2")
    public ResponseEntity<Page<Obj>> getPage(@RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer per_page,
                                             @RequestParam(required = false) String sort,
                                             @RequestParam(required = false) Boolean desc
    ) throws Exception {
        Page<Obj> objPage = null;
        List<String> sorts = List.of("create_time", "update_time", "file_size");
        if (sort == null) {
            objPage = objService.selectByPage((long) page, (long) per_page);
        }
        else if(sorts.contains(sort)) {
            objPage = objService.selectByPageSort((long) page, (long) per_page, sort, desc);
        }else
            throw new BaseException("sort choice '" +sort + "' is illegal");
        return new ResponseEntity<>(objPage, HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<Obj> getOneByUUid(@PathVariable String uuid) throws Exception {
        Obj obj = objService.selectByUUid(uuid);
        return new ResponseEntity<>(obj,HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<Obj>> listSelf() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Obj> objs = objService.selectByCreator(user.getId());
        return new ResponseEntity<>(objs, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) throws Exception {

        Obj obj = objService.selectById(id);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setDelete(true);
        objService.update(obj, obj.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(obj.getUid());
        recycle.setType(3);
        recycle.setAuthor(obj.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteByUuid(@PathVariable("uuid") String uuid) throws Exception {

        Obj obj = objService.selectByUUid(uuid);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setDelete(true);
        objService.update(obj, obj.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(obj.getUid());
        recycle.setType(3);
        recycle.setAuthor(obj.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/update")
    public ResponseEntity<Obj> updateByUuid(@PathVariable("uuid") String uuid, @RequestBody Obj entity) throws Exception {
        Obj old = objService.selectByUUid(uuid);
        if (entity == null)
            throw new NotFoundException("entity not found!");
        Obj res = objService.update(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<Obj> setTitleByUuid(@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        Obj entity = objService.selectByUUid(uuid);
        if (entity == null)
            throw new NotFoundException("entity not found!");
        entity.setTitle(title);
        Obj res = objService.update(entity, entity.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<Obj> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        Obj obj = objService.selectByUUid(uuid);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setFolder(folderUid);
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setDescription")
    public ResponseEntity<Obj> setDescriptionByUuid(@PathVariable("uuid") String uuid, @RequestParam String description) throws Exception {
        Obj obj = objService.selectByUUid(uuid);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setDescription(description);
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<Obj> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        Obj obj = objService.selectByUUid(uuid);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setThumb(cover);
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<Obj> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        Obj obj = objService.selectByUUid(uuid);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setAddress(address);
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTags")
    public ResponseEntity<Obj> setTags(@PathVariable("uuid") String uuid, @RequestBody List<String> tagUidLists) throws Exception {
        Obj obj = objService.selectByUUid(uuid);
        objService.setTags(obj.getId(), tagUidLists);
        return new ResponseEntity<>(objService.selectByUUid(uuid), HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setContents")
    public ResponseEntity<Obj> setContents(@PathVariable("uuid") String uuid, @RequestBody JSON contents) throws Exception {
        Obj obj = objService.selectByUUid(uuid);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setContents(JSON.toJSONString(contents));
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<Obj> update (@PathVariable("uuid") String uuid, @RequestBody Obj entity) throws Exception {
        Obj old = objService.selectByUUid(uuid);
        if (old == null)
            throw new NotFoundException("obj not found!");
        entity.setUid(old.getUid());
        Obj res = objService.update(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        Obj entity = objService.selectByUUid(uuid);
        if (entity == null)
            throw new NotFoundException("Folder not found!");
        // entity.setCollect(collect);
        objService.update(entity, entity.getId());
        if (collect) {
            Collection collection = new Collection();
            collection.setUid(entity.getUid());
            collection.setType(3);
            collection.setAuthor(entity.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected", HttpStatus.OK);
        } else {
            collectionService.deleteObj(entity.getUid(), entity.getAuthor());
            return new ResponseEntity<>("dismiss collection", HttpStatus.OK);
        }
    }

    @PatchMapping("{id}/setFolder")
    public ResponseEntity<Obj> setFolder(@PathVariable("id") Integer id, @RequestParam String folderUid) throws Exception {
        Obj marker = objService.selectById(id);
        if (marker == null)
            throw new NotFoundException("obj not found!");
        marker.setFolder(folderUid);
        Obj res = objService.update(marker, marker.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setDescription")
    public ResponseEntity<Obj> setDescription (@PathVariable("id") Integer id, @RequestParam String description) throws Exception {
        Obj obj = objService.selectById(id);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setDescription(description);
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<Obj> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        Obj obj = objService.selectById(id);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setThumb(cover);
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<Obj> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        Obj obj = objService.selectById(id);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setAddress(address);
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setTags")
    public ResponseEntity<Obj> setTags(@PathVariable("id") Integer id, @RequestBody List<String> tagUidLists) throws Exception {
        Obj obj = objService.selectById(id);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        objService.setTags(id, tagUidLists);
        return new ResponseEntity<>(objService.selectById(id), HttpStatus.OK);
    }

    @PatchMapping("{id}/setContents")
    public ResponseEntity<Obj> setContents(@PathVariable("id") Integer id, @RequestBody JSON contents) throws Exception {
        Obj obj = objService.selectById(id);
        if (obj == null)
            throw new NotFoundException("obj not found!");
        obj.setContents(JSON.toJSONString(contents));
        Obj res = objService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
