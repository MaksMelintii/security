package com.example.security.item;

/*
@author   maksm
@project   security
@class  ItemRestController
@version  1.0.0
@since 30.10.2025 - 11.00
*/
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
public class ItemRestController {
    @Autowired
    private  ItemService service;

    public ItemRestController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<Item> getItems() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Item getOneItem(@PathVariable String id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.deleteById(id);
    }

    @PostMapping
    public Item create(@RequestBody Item item) {
        return service.create(item);
    }

    @PutMapping
    public Item update(@RequestBody Item item) {
        return service.update(item);
    }

    @GetMapping("/hello/user")
    public String helloUser() {
        return "Hello User";
    }

    @GetMapping("/hello/admin")
    public String helloAdmin() {
        return "Hello Admin";
    }

    @GetMapping("/hello/unknown")
    public String helloUnknown() {
        return "Hello Unknown";
    }

//    @RestController
//    public class TestController {
//        @GetMapping("/hello/user")
//        public String helloUser() {
//            return "Hello User!";
//        }
//    }









}
