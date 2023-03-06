package com.xmap.v04.controller;

import com.alibaba.fastjson.JSON;
import com.xmap.v04.ResultEntiy.BusinessException;
import com.xmap.v04.ResultEntiy.ResultEnum;
import com.xmap.v04.ResultEntiy.ResultResponse;
import com.xmap.v04.config.security.JwtUtil;
import com.xmap.v04.entity.*;
import com.xmap.v04.exception.BaseException;
import com.xmap.v04.exception.ParamsMissException;
import com.xmap.v04.models.LoginResponseModel;
import com.xmap.v04.models.NewPasswordModel;
import com.xmap.v04.models.UserRequestModel;
import com.xmap.v04.service.*;
import com.xmap.v04.utils.CheckUtil;
import com.xmap.v04.utils.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;


@RestController
@Api(tags = "用户接口")
@RequestMapping("api/user")
public class UserController {
    private final String changePassword = "changePassword";
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private BucketService bucketService;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    MinioUtil minioUtil;

    @Autowired
    private DefaultRecordService defaultRecordService;

    @Autowired
    private DataPackageService dataPackageService;

    @Autowired
    private FolderService sceneService;

    @Autowired
    private FolderService objectService;

    @Autowired
    private FolderService folderService;

    Timestamp get_time() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }



    @GetMapping("info")
    @ApiOperation(value = "查询用户")
    public BusinessException userInfo(String username) throws Exception {

        boolean isEmail = CheckUtil.isEmail(username);
        boolean isPhone = CheckUtil.isPhone(username);
        return  new BusinessException(ResultEnum.NO_PERMISSION.getCode(),ResultEnum.NO_PERMISSION.getMessage());

        //校验是否为邮箱或者手机号
//        User user =  userService.findUserByName(username);//查询用户
//
////         这一块用 fastjson 或者 jackson 封装替代
//        if(user == null)
//            throw new NotFoundException("user"); //初始化user类
//
//             //return new ResponseContent(CodeEnum.OK.getCode(), CodeEnum.OK.getDesc(),data);
//
//           return new BusinessException(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage(),user);

           // return new ResponseEntity<>(user, HttpStatus.OK);//调用注册逻辑



    }



    public String GetCheckUser(String userName,String password, User user){

        boolean isEmail = CheckUtil.isEmail(userName);
        boolean isPhone = CheckUtil.isPhone(userName);
        String msg = null;
        if(user!=null){//用户已存在
            msg = "该账号已经注册";
        }
        else if(password == null || userName == null) {
            msg = "请输入用户名或密码";
        }
        else if (!isEmail&&!isPhone) {//校验账号
            msg = "只能输入手机号或者邮箱号";
        }
        return  msg;

    }


    @PostMapping(value = "register")
    @ApiOperation(value = "注册")
    public Object register (@RequestBody UserRequestModel userModel) throws Exception {

        String username = userModel.getUsername();
        String password = userModel.getPassword();
        User finduser =  userService.findUserByName(username);//查询用户
        String msg =  GetCheckUser(username,password,finduser);//校验账号
        User user = userService.regist(username, password);
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generatRefreshToken(user);
        if(msg != null){
            logger.info("msg====="+msg);
           return new ResultResponse<>(ResultEnum.NOT_FOUND,msg);
        }
        boolean exists = minioUtil.createBucket(user.getUuid());//创建桶
        if (exists)
            bucketService.addBucket(username, user.getId());
            Folder folder = new Folder();//默认数据包文件夹
            folder.setName("DATAPACKKTS");
            folder.setAuthor(user.getId());
            folder.setCreateTime(get_time());
            Folder addFolder = folderService.addFolder(folder);

            Folder object = new Folder();//默认物体文件夹
            object.setName("OBJECTS");
            object.setAuthor(user.getId());
            object.setCreateTime(get_time());
            Folder addObject = objectService.addFolder(object);

            Folder scene = new Folder();//默认场景文件夹
            scene.setName("SCENES");
            scene.setAuthor(user.getId());
            scene.setCreateTime(get_time());
            Folder addScene = sceneService.addFolder(scene);

            DefaultRecord defaultRecord = new DefaultRecord();
            defaultRecord.setOwner(user.getUuid());
            defaultRecord.setObject(addObject.getUuid());
            defaultRecord.setScene(addScene.getUuid());
            defaultRecord.setFolder(addFolder.getUuid());

            defaultRecordService.addRecord(defaultRecord);

            String defaultData = JSON.toJSONString(defaultRecord);

            LoginResponseModel resutlt =  new LoginResponseModel(accessToken, refreshToken, user.getUuid(), defaultData);
            //throw new BusinessException(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMessage());
            return new ResultResponse<>(ResultEnum.SUCCESS,"成功",resutlt);


         //throw new BusinessException(ResultEnum.NO_PERMISSION.getCode(), "桶创建失败");

//
//return new ResponseEntity<>(new LoginResponseModel(accessToken, refreshToken, user.getUuid(), defaultData), HttpStatus.CREATED);
//

    }

    @PostMapping(value = "login")
    @ApiOperation(value = "登录")
    public Object createAuthenticationToken(@RequestBody @Validated UserRequestModel userModel) throws Exception {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword())
        );

        User user = userService.loadUserByUsername(userModel.getUsername());

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generatRefreshToken(user);
        DefaultRecord defaultRecord = defaultRecordService.getByOwner(user.getUuid());
        String defaultData = "";
        logger.info("accessToken:"+accessToken+"refreshToken:"+refreshToken);
        if(defaultRecord != null) {
            defaultData = JSON.toJSONString(defaultRecord);
        }else
            defaultData = null;

        logger.info("defaultData:"+defaultData);

        LoginResponseModel resutlt =  new LoginResponseModel(accessToken, refreshToken, user.getUuid(), defaultData);


        return new ResultResponse<>(ResultEnum.SUCCESS,"成功",resutlt);

        //return ResponseEntity.ok(new LoginResponseModel(accessToken, refreshToken, user.getUuid(), defaultData));

    }
    @PostMapping(value = "changePassword")
    @ApiOperation(value = "修改密码")
    public Object changePassword(@RequestBody @Validated NewPasswordModel newPasswordModel) throws Exception {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(newPasswordModel.getUsername(), newPasswordModel.getPassword())
        );

        User user = userService.loadUserByUsername(newPasswordModel.getUsername());

        userService.changePassword(newPasswordModel.getNewPassword(), user.getId());

         ResponseEntity.status(HttpStatus.CREATED).body("密码已更改");

        logger.info("getUsername====="+newPasswordModel.getUsername()+"getPassword"+newPasswordModel.getPassword());
        String userName =  newPasswordModel.getUsername();

        HashMap<String,String> map = new HashMap<String, String>(){{
            put("userName",userName);
        }};
        return new ResultResponse<>(ResultEnum.SUCCESS,"成功",map);

       // return ResponseEntity.status(HttpStatus.CREATED).body("password has changed.");
    }

    @PostMapping(value = "update")
    public ResponseEntity<User> updateInfo(@RequestBody User updates) throws Exception {
        // 获取当前用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        int id = user.getId();
        updates.setId(id);
        user = userService.update(updates);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}