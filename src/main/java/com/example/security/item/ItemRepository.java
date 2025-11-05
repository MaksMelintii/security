package com.example.security.item;

/*
@author   maksm
@project   security
@class  ItemRepository
@version  1.0.0
@since 30.10.2025 - 11.00
*/
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<Item, String> {

}
