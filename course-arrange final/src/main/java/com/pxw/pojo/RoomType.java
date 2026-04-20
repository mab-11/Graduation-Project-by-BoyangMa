package com.pxw.pojo;

import lombok.Data;

/**
 * Room type entity class
 */
@Data
public class RoomType {
    private Integer id;
    private Integer roomId;
    private String typeName;  // Type name: regular classroom, multimedia classroom, laboratory, gymnasium, etc.

    public RoomType() {}

    public RoomType(Integer roomId, String typeName) {
        this.roomId = roomId;
        this.typeName = typeName;
    }
}
