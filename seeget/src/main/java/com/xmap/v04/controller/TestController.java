package com.xmap.v04.controller;


import com.alibaba.fastjson.JSON;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class Command extends Thread {
    private Thread t;
    private String comm;

    Command( String command) {
        this.comm = command;
    }

    public void run() {
        try{
            BufferedReader br = null;
            try {
                Process p = Runtime.getRuntime().exec(comm);
                br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                System.out.println(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally
            {
                if (br != null)
                {
                    try {
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void start () {
        System.out.println("Starting " +  comm );
        if (t == null) {
            t = new Thread (this, comm);
            t.start ();
        }
    }
}

@RestController
@RequestMapping("api/test")
public class TestController {
    Logger logger = LoggerFactory.getLogger(TestController.class);

    @PostMapping("gitlab")
    public ResponseEntity<String> test(@RequestBody JSON test) {
        logger.info("gitlab");
        logger.info(test.toJSONString());
        String commandStr = "sh /home/admin/gitlab-script/build.sh";
        Command command = new Command(commandStr);
        command.start();
        return new ResponseEntity<>("1", HttpStatus.OK);
    }
}
