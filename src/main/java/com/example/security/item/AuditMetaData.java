package com.example.security.item;

/*
@author   maksm
@project   security
@class  AuditMetaData
@version  1.0.0
@since 12.11.2025 - 15.42
*/

import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

public class AuditMetaData {
    @CreatedDate
    private LocalDateTime createdDate;
    @CreatedBy
    private String createdBy;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    @LastModifiedBy
    private String lastModifiedBy;


}
