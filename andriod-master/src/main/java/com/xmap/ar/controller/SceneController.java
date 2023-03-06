package com.xmap.ar.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xmap.ar.entity.*;
import com.xmap.ar.exception.BaseException;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.service.CollectionService;
import com.xmap.ar.service.RecycleService;
import com.xmap.ar.service.SceneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/scene")
public class SceneController {
    Logger logger = LoggerFactory.getLogger(SceneController.class);

    @Autowired
    private SceneService sceneService;

    @Autowired
    private RecycleService recycleService;

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<Scene> create(@RequestBody Scene scene) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Scene newScene = sceneService.addScene(scene, user.getId());
        return new ResponseEntity<>(newScene, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Scene>> getAll() {
        List<Scene> scenes = sceneService.selectALL();
        return new ResponseEntity<>(scenes, HttpStatus.OK);
    }

    @GetMapping("v2")
    public ResponseEntity<Page<Scene>> getPage(@RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer per_page,
                                               @RequestParam(required = false) String sort,
                                               @RequestParam(required = false) Boolean desc
    ) throws Exception {
        Page<Scene> scenePage = null;
        List<String> sorts = List.of("create_time", "update_time", "file_size");
        if (sort == null) {
            scenePage = sceneService.selectByPage((long) page, (long) per_page);
        }
        else if(sorts.contains(sort)) {
            scenePage = sceneService.selectByPageSort((long) page, (long) per_page, sort, desc);
        }else
            throw new BaseException("sort choice '" +sort + "' is illegal");
        return new ResponseEntity<>(scenePage, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Scene> getOne(@PathVariable int id) throws Exception {
        Scene scene = sceneService.selectById(id);
        return new ResponseEntity<>(scene,HttpStatus.OK);
    }

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<Scene> getOneByUUid(@PathVariable String uuid) throws Exception {
        Scene scene = sceneService.selectByUUid(uuid);
        return new ResponseEntity<>(scene,HttpStatus.OK);
    }

    @GetMapping("self")
    public ResponseEntity<List<Scene>> listSelf() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<Scene> scenes = sceneService.selectByCreator(user.getId());
        return new ResponseEntity<>(scenes, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteById(@PathVariable int id) throws Exception {

        Scene scene = sceneService.selectById(id);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        scene.setDelete(true);
        sceneService.update(scene, scene.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(scene.getUid());
        recycle.setType(4);
        recycle.setAuthor(scene.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @DeleteMapping("uuid/{uuid}")
    public ResponseEntity<String> deleteByUuid(@PathVariable("uuid") String uuid) throws Exception {

        Scene scene = sceneService.selectByUUid(uuid);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        scene.setDelete(true);
        sceneService.update(scene, scene.getId());
        Recycle recycle = new Recycle();
        recycle.setUid(scene.getUid());
        recycle.setType(3);
        recycle.setAuthor(scene.getAuthor());
        recycleService.add(recycle);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/update")
    public ResponseEntity<Scene> updateByUuid(@PathVariable("uuid") String uuid, @RequestBody Scene entity) throws Exception {
        Scene old = sceneService.selectByUUid(uuid);
        if (entity == null)
            throw new NotFoundException("entity not found!");
        Scene res = sceneService.update(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTitle")
    public ResponseEntity<Scene> setTitleByUuid(@PathVariable("uuid") String uuid, @RequestParam String title) throws Exception {
        Scene entity = sceneService.selectByUUid(uuid);
        if (entity == null)
            throw new NotFoundException("entity not found!");
        entity.setTitle(title);
        Scene res = sceneService.update(entity, entity.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setFolder")
    public ResponseEntity<Scene> setFolderByUuid(@PathVariable("uuid") String uuid, @RequestParam String folderUid) throws Exception {
        Scene scene = sceneService.selectByUUid(uuid);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        scene.setFolder(folderUid);
        Scene res = sceneService.update(scene, scene.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setDescription")
    public ResponseEntity<Scene> setDescriptionByUuid(@PathVariable("uuid") String uuid, @RequestParam String description) throws Exception {
        Scene scene = sceneService.selectByUUid(uuid);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        scene.setDescription(description);
        Scene res = sceneService.update(scene, scene.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCover")
    public ResponseEntity<Scene> setCoverByUuid (@PathVariable("uuid") String uuid, @RequestParam String cover) throws Exception {
        Scene scene = sceneService.selectByUUid(uuid);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        scene.setThumb(cover);
        Scene res = sceneService.update(scene, scene.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setAddress")
    public ResponseEntity<Scene> setAddressByUuid (@PathVariable("uuid") String uuid, @RequestParam String address) throws Exception {
        Scene scene = sceneService.selectByUUid(uuid);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        scene.setAddress(address);
        Scene res = sceneService.update(scene, scene.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setTags")
    public ResponseEntity<Scene> setTags(@PathVariable("uuid") String uuid, @RequestBody List<String> tagUidLists) throws Exception {
        Scene scene = sceneService.selectByUUid(uuid);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        sceneService.setTags(scene.getId(), tagUidLists);
        return new ResponseEntity<>(sceneService.selectByUUid(uuid), HttpStatus.OK);
    }

    @PutMapping("uuid/{uuid}/update")
    public ResponseEntity<Scene> update (@PathVariable("uuid") String uuid, @RequestBody Scene entity) throws Exception {
        Scene old = sceneService.selectByUUid(uuid);
        if (old == null)
            throw new NotFoundException("scene not found!");
        entity.setUid(old.getUid());
        Scene res = sceneService.update(entity, old.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("uuid/{uuid}/setCollect")
    public ResponseEntity<String> setCollect(@PathVariable("uuid") String uuid, @RequestParam Boolean collect) throws Exception {
        Scene entity = sceneService.selectByUUid(uuid);
        if (entity == null)
            throw new NotFoundException("Folder not found!");
        // entity.setCollect(collect);
        sceneService.update(entity, entity.getId());
        if (collect) {
            Collection collection = new Collection();
            collection.setUid(entity.getUid());
            collection.setType(4);
            collection.setAuthor(entity.getAuthor());
            collectionService.add(collection);
            return new ResponseEntity<>("collected", HttpStatus.OK);
        } else {
            collectionService.deleteObj(entity.getUid(), entity.getAuthor());
            return new ResponseEntity<>("dismiss collection", HttpStatus.OK);
        }
    }

    @PatchMapping("{id}/setFolder")
    public ResponseEntity<Scene> setFolder(@PathVariable("id") Integer id, @RequestParam String folderUid) throws Exception {
        Scene scene = sceneService.selectById(id);
        if (scene == null)
            throw new NotFoundException("scene not found!");
        scene.setFolder(folderUid);
        Scene res = sceneService.update(scene, scene.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setDescription")
    public ResponseEntity<Scene> setDescription (@PathVariable("id") Integer id, @RequestParam String description) throws Exception {
        Scene obj = sceneService.selectById(id);
        if (obj == null)
            throw new NotFoundException("scene not found!");
        obj.setDescription(description);
        Scene res = sceneService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setCover")
    public ResponseEntity<Scene> setCover (@PathVariable("id") Integer id, @RequestParam String cover) throws Exception {
        Scene obj = sceneService.selectById(id);
        if (obj == null)
            throw new NotFoundException("scene not found!");
        obj.setThumb(cover);
        Scene res = sceneService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setAddress")
    public ResponseEntity<Scene> setAddress (@PathVariable("id") Integer id, @RequestParam String address) throws Exception {
        Scene obj = sceneService.selectById(id);
        if (obj == null)
            throw new NotFoundException("scene not found!");
        obj.setAddress(address);
        Scene res = sceneService.update(obj, obj.getId());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("{id}/setTags")
    public ResponseEntity<Scene> setTags(@PathVariable("id") Integer id, @RequestBody List<String> tagUidLists) throws Exception {
        sceneService.setTags(id, tagUidLists);
        return new ResponseEntity<>(sceneService.selectById(id), HttpStatus.OK);
    }

}
