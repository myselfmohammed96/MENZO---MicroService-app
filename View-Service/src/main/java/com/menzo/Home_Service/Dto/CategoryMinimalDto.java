package com.menzo.Home_Service.Dto;

public class CategoryMinimalDto {

    private Long id;
    private String categoryName;
    private String categoryBannerImg;

    public CategoryMinimalDto() {}

    public CategoryMinimalDto(Long id, String categoryName, String categoryBannerImg) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryBannerImg = categoryBannerImg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryBannerImg() {
        return categoryBannerImg;
    }

    public void setCategoryBannerImg(String categoryBannerImg) {
        this.categoryBannerImg = categoryBannerImg;
    }

    public String toString() {
        return "CategoryMinimalDto:\nid: " + id + "\ncategoryName: " + categoryName +
                "\ncategoryBannerImg: " + categoryBannerImg;
    }
}
