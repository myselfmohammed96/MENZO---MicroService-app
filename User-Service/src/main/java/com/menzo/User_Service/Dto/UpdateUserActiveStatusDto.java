package com.menzo.User_Service.Dto;

public class UpdateUserActiveStatusDto {

    private Long userId;
    private boolean block;

    public UpdateUserActiveStatusDto() {}

    public UpdateUserActiveStatusDto(Long userId, boolean block) {
        this.userId = userId;
        this.block = block;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public void display() {
        System.out.println("UpdateUserActiveStatusDto:\nuserId: " + userId +
                "\nblock: " + block);
    }
}
