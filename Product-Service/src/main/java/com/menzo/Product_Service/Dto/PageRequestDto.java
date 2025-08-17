//package com.menzo.Product_Service.Dto;
//
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Objects;
//
//public class PageRequestDto {
//
//    private Integer pageNo = 0;
//    private Integer pageSize = 10;
//
//    public Pageable getPageable(PageRequestDto dto) {
//        Integer page = Objects.nonNull(dto.getPageNo()) ? dto.getPageNo() : this.pageNo;
//        Integer size = Objects.nonNull(dto.getPageSize()) ? dto.getPageSize() : this.pageSize;
//
//        PageRequest request = PageRequest.of(page, size);
//        return request;
//    }
//
//    public Integer getPageNo() {
//        return pageNo;
//    }
//
//    public void setPageNo(Integer pageNo) {
//        this.pageNo = pageNo;
//    }
//
//    public Integer getPageSize() {
//        return pageSize;
//    }
//
//    public void setPageSize(Integer pageSize) {
//        this.pageSize = pageSize;
//    }
//}
