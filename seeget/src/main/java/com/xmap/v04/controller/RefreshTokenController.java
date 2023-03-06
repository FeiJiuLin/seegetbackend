package com.xmap.v04.controller;

import com.alibaba.fastjson.JSON;
import com.xmap.v04.config.security.JwtUtil;
import com.xmap.v04.entity.DefaultRecord;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.BaseException;
import com.xmap.v04.models.LoginResponseModel;
import com.xmap.v04.service.DefaultRecordService;
import com.xmap.v04.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("api")
public class RefreshTokenController {
    private final Logger logger = LoggerFactory.getLogger(RefreshTokenController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private DefaultRecordService defaultRecordService;

    @GetMapping("refreshtoken/{token}")
    public ResponseEntity<LoginResponseModel> refreshToken(@PathVariable("token") String token) throws Exception{
        // Get jwt token and validate

        if (!jwtUtil.validate(token))
            throw new BaseException("token is illegal.");

        //TODO distinct refresh token between access token
        if (Objects.equals(jwtUtil.getIssuer(token), "refresh")) {
            // Get user identity and set it on the spring security context
            try {
                User user = userService.loadUserByUsername((jwtUtil.getUsername(token)));
                String accessToken = jwtUtil.generateAccessToken(user);
                DefaultRecord defaultRecord = defaultRecordService.getByOwner(user.getUuid());
                String defaultData = "";
                if(defaultRecord != null) {
                    defaultData = JSON.toJSONString(defaultRecord);
                }else
                    defaultData = null;
                return new ResponseEntity<>(new LoginResponseModel(accessToken, token, user.getUuid(), defaultData), HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getMessage());
                throw new BaseException(e.toString());
            }
        } else {
            throw new BaseException("token is illegal.");
        }


    }
}
