package com.xmap.v04.controller;

import com.xmap.v04.models.Fileinfo;
import com.xmap.v04.utils.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "文件操作接口")
@RestController
@RequestMapping(value = "/minio")
public class MinioController {

    @Autowired
    MinioUtil minioUtil;

    @ApiOperation("upload file")
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public ResponseEntity<String> fileupload(@RequestParam MultipartFile uploadfile, @RequestParam String bucket,
                                     @RequestParam(required = false) String objectName) throws Exception {
        minioUtil.createBucket(bucket);
        if (objectName != null) {
            minioUtil.uploadFile(uploadfile.getInputStream(), bucket, objectName + "/" + uploadfile.getOriginalFilename());
        } else {
            minioUtil.uploadFile(uploadfile.getInputStream(), bucket, uploadfile.getOriginalFilename());
        }
        return new ResponseEntity<>("uploaded!", HttpStatus.OK);
    }

    @ApiOperation("list all buckets")
    @RequestMapping(value = "/listBuckets", method = RequestMethod.GET)
    public ResponseEntity<List<String>> listBuckets() throws Exception {
        return new ResponseEntity<>(minioUtil.listBuckets(), HttpStatus.OK);
    }

    @ApiOperation("list all files")
    @RequestMapping(value = "/listFiles", method = RequestMethod.GET)
    public ResponseEntity<List<Fileinfo>> listFiles(@RequestParam String bucket) throws Exception {
        return new ResponseEntity<>(minioUtil.listFiles(bucket), HttpStatus.OK);
    }

    @ApiOperation("download")
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam String bucket, @RequestParam String objectName,
                             HttpServletResponse response) throws Exception {
        InputStream stream = minioUtil.download(bucket, objectName);
        ServletOutputStream output = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), "UTF-8"));
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        IOUtils.copy(stream, output);
    }


    @ApiOperation("delete a file from a bucket")
    @RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
    public ResponseEntity<String> deleteFile(@RequestParam String bucket, @RequestParam String objectName) throws Exception {
        minioUtil.deleteObject(bucket, objectName);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @ApiOperation("delet bucket")
    @RequestMapping(value = "/deleteBucket", method = RequestMethod.GET)
    public ResponseEntity<String> deleteBucket(@RequestParam String bucket) throws Exception {
        minioUtil.deleteBucket(bucket);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }


    @ApiOperation("copy object")
    @GetMapping("/copyObject")
    public ResponseEntity<String> copyObject(@RequestParam String sourceBucket, @RequestParam String sourceObject, @RequestParam String targetBucket, @RequestParam String targetObject) throws Exception {
        minioUtil.copyObject(sourceBucket, sourceObject, targetBucket, targetObject);
        return new ResponseEntity<>("copyed!", HttpStatus.OK);
    }

    @GetMapping("/getObjectInfo")
    @ApiOperation("get object info")
    public ResponseEntity<String> getObjectInfo(@RequestParam String bucket, @RequestParam String objectName) throws Exception {

        return new ResponseEntity<>(minioUtil.getObjectInfo(bucket, objectName), HttpStatus.OK);
    }

    @GetMapping("/getPresignedObjectUrl")
    @ApiOperation("get objecet download url")
    public ResponseEntity<String> getPresignedObjectUrl(@RequestParam String bucket, @RequestParam String objectName, @RequestParam Integer expires) throws Exception {

        return new ResponseEntity<>(minioUtil.getPresignedObjectUrl(bucket, objectName, expires), HttpStatus.OK);
    }

    @GetMapping("/listAllFile")
    @ApiOperation("get all file")
    public ResponseEntity<List<Fileinfo>> listAllFile() throws Exception {

        return new ResponseEntity<>(minioUtil.listAllFile(), HttpStatus.OK);
    }


}