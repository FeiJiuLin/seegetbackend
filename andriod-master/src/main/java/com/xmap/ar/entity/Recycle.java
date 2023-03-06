package com.xmap.ar.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data@AllArgsConstructor@NoArgsConstructor@Getter@Setter
@TableName("recycle")
public class Recycle {
    @TableId(value = "id", type = IdType.AUTO)
    @JSONField(serialize = false)
    private Integer id;

    private Integer type;

    private String uid;

    private Integer author;

}
