package com.xmap.ar.controller;

import com.xmap.ar.config.security.JwtUtil;
import com.xmap.ar.entity.User;
import com.xmap.ar.exception.BaseException;
import com.xmap.ar.exception.NotFoundException;
import com.xmap.ar.exception.ParamsMissException;
import com.xmap.ar.model.LoginResponseModel;
import com.xmap.ar.model.NewPasswordModel;
import com.xmap.ar.model.UserRequestModel;
import com.xmap.ar.service.BucketService;
import com.xmap.ar.service.UserService;
import com.xmap.ar.util.MinioUtils;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
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
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Api(tags = "用户接口")
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MinioUtils minioUtil;
    @Autowired
    private BucketService bucketService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    private MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://127.0.0.1:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    //        private MinioClient minioClient =
//            MinioClient.builder()
//                    .endpoint("http://127.0.0.1:9000")
//                    .credentials("minioadmin", "cvrs213minio#@!")
//                    .build();
    @GetMapping("info")
    @ApiOperation(value = "查询用户")
    public ResponseEntity<User> userInfo(String phone) throws Exception {
        User user =  userService.findUserByPhone(phone);
//         这一块用 fastjson 或者 jackson 封装替代
        if(user == null)
            throw new NotFoundException("user");

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "register")
    @ApiOperation(value = "注册")
    public ResponseEntity<LoginResponseModel>  register (@RequestBody UserRequestModel userModel) throws Exception {
        String phone = userModel.getPhone();
        String password = userModel.getPassword();
        logger.info(userModel.toString());
        if(password == null || phone == null) {
            throw new ParamsMissException("phone or password");
        } else if(userService.findUserByPhone(phone) != null) {
            throw new BaseException("phone already exists.");
        } else {

            Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
            Matcher m = p.matcher(phone);
            if(!m.matches())
                throw new BaseException("phone is illegal");

            User user = userService.regist(phone, password);
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generatRefreshToken(user);
            minioUtil.createBucket(user.getUid());
            logger.info("create bucket for new user:"+ phone);
            bucketService.addBucket(user.getPhone(), user.getId());
            return new ResponseEntity<>(new LoginResponseModel(accessToken, refreshToken), HttpStatus.CREATED);
        }
    }

    @PostMapping(value = "login")
    @ApiOperation(value = "登录")
    public ResponseEntity<LoginResponseModel> createAuthenticationToken(@RequestBody UserRequestModel userModel) throws Exception {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userModel.getPhone(), userModel.getPassword())
        );
        User user = userService.loadUserByUsername(userModel.getPhone());

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generatRefreshToken(user);
        return ResponseEntity.ok(new LoginResponseModel(accessToken, refreshToken));

    }

    @GetMapping("refreshtoken/{token}")
    public ResponseEntity<LoginResponseModel> refreshToken(@PathVariable("token") String token) throws Exception{
        // Get jwt token and validate

        if (!jwtUtil.validate(token))
            throw new BaseException("token is illegal.");

        if (Objects.equals(jwtUtil.getIssuer(token), "refresh")) {
            // Get user identity and set it on the spring security context
            try {
                User user = userService.loadUserByUsername((jwtUtil.getPhone(token)));
                String accessToken = jwtUtil.generateAccessToken(user);
                return new ResponseEntity<>(new LoginResponseModel(accessToken, token), HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new BaseException(e.toString());
            }
        } else {
            throw new BaseException("token is illegal.");
        }
    }

    @PostMapping(value = "changePassword")
    public ResponseEntity<String> chandePassword(@RequestBody NewPasswordModel newPasswordModel) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(newPasswordModel.getPhone(), newPasswordModel.getPassword())
        );

        User user = userService.loadUserByUsername(newPasswordModel.getPhone());

        userService.changePassword(newPasswordModel.getNewPassword(), user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("password has changed.");
    }

    @PatchMapping(value = "changeName")
    public ResponseEntity<User> changeName(@RequestParam String name) throws Exception {
        // 获取当前用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setName(name);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setAvatar")
    public ResponseEntity<User> setAvatar(@RequestParam String avatarKey) throws Exception{
        return null;
    }

    @PatchMapping(value = "setJob")
    public ResponseEntity<User> setJob(@RequestParam String job) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setJob(job);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setCity")
    public ResponseEntity<User> setCity(@RequestParam String city) throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setCity(city);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setAge")
    public ResponseEntity<User> setAge(@RequestParam Integer age) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setAge(age);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setBirthday")
    public ResponseEntity<User> setBirthDay(@RequestParam Timestamp birthday) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBirthday(birthday);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setFullName")
    public ResponseEntity<User> setFullName(@RequestParam String fullname) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setFullName(fullname);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setGender")
    public ResponseEntity<User> setGender(@RequestParam Boolean gender) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setGender(gender);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setIdcard")
    public ResponseEntity<User> setIdcard(@RequestParam String idcard) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setIdCard(idcard);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setBank")
    public ResponseEntity<User> setBank(@RequestParam String bank) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBankCard(bank);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setCardNumber")
    public ResponseEntity<User> setCardNumber(@RequestParam String cardNumber) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBankCardNumber(cardNumber);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PatchMapping(value = "setNation")
    public ResponseEntity<User> setNation(@RequestParam String nation) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setNation(nation);
        user = userService.update(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteUser() throws Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.delete(user.getId());
        return new ResponseEntity<>("delete!", HttpStatus.OK);
    }

}
