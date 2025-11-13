package com.example.security;

/*
@author   maksm
@project   security
@class  AccessTests
@version  1.0.0
@since 12.11.2025 - 16.20
*/

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AccessTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    void deforeAll(){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }
    //  1. Анонім -> /api/v1/items => 401
    @Test
    @WithAnonymousUser
    public void whenAnonymThenStatusUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/items"))
                .andExpect(status().isUnauthorized());

    }
    // 2 ADMIN -> /hello/admin => 200
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void whenAuthenticatedThenStatusOk() throws Exception {

        mockMvc.perform(get("/api/v1/items/hello/admin"))
                .andExpect(status().isOk());
    }
    // 3. ADMIN -> /hello/user => 403
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void whenAuthenticatedThenStatus403() throws Exception {

        mockMvc.perform(get("/api/v1/items/hello/user"))
                .andExpect(status().isForbidden());
    }

    // 4. USER -> /hello/user => 200
    @Test
    @WithMockUser(username = "user",password = "user", roles = {"USER"})
    void whenUserAccessUserEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/user"))
                .andExpect(status().isOk());
    }

    // 5. USER -> /hello/admin => 403
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void whenUserAccessAdminEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/admin"))
                .andExpect(status().isForbidden());
    }

    // 6. USER -> /hello/superadmin => 403
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void whenUserAccessSuperAdminEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/superadmin"))
                .andExpect(status().isForbidden());
    }

    // 7. SUPERADMIN -> /hello/superadmin => 200
    @Test
    @WithMockUser(username = "superadmin", password = "superadmin", roles = {"SUPERADMIN"})
    void whenSuperAdminAccessSuperAdminEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/superadmin"))
                .andExpect(status().isOk());
    }

    // 8. SUPERADMIN -> /hello/admin => 403 (бо не має ролі ADMIN)
    @Test
    @WithMockUser(username = "superadmin", password = "superadmin", roles = {"SUPERADMIN"})
    void whenSuperAdminAccessAdminEndpoint_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/admin"))
                .andExpect(status().isForbidden());
    }

    // 9. ADMIN -> /api/v1/items/create => 200
    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
    void whenAdminCreateItem_thenOk() throws Exception {
        String newItemJson = """
        {
            "name": "Test Item",
            "price": 123.45
        }
        """;

        mockMvc.perform(post("/api/v1/items/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newItemJson))
                .andExpect(status().isOk());
    }

    // 10. SUPERADMIN -> /api/v1/items/create => 200
    @Test
    @WithMockUser(username = "superadmin", password = "superadmin", roles = {"ADMIN"})
    void whenSuperadminCreateItem_thenOk() throws Exception {
        String newItemJson = """
        {
            "id": "1",
            "name": "Shirt",
            "description": "Blue cotton shirt"
        }
        """;

        mockMvc.perform(post("/api/v1/items/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newItemJson))
                .andExpect(status().isOk());
    }

    // 11. SUPERADMIN -> /api/v1/items/delete/1 => 200
    @Test
    @WithMockUser(username = "superadmin",password = "superadmin" , roles = {"SUPERADMIN"})
    void whenSuperAdminDeleteItem_thenOk() throws Exception {
        mockMvc.perform(delete("/api/v1/items/delete/1"))
                .andExpect(status().isOk());
    }

    // 12 USER -> /api/v1/items/create => 403
    @Test
    @WithMockUser(username = "user", password = "user", roles = {"USER"})
    void whenUserTriesToCreateItem_thenForbidden() throws Exception {
        String newItemJson = """
        {
            "id": "1",
            "name": "Shirt",
            "description": "Blue cotton shirt"
        }
        """;

        mockMvc.perform(post("/api/v1/items/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newItemJson))
                .andExpect(status().isForbidden());
    }

    // 13 USER -> /api/v1/items/delete/1 => 403
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void whenUserTriesToDeleteItem_thenForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/items/delete/1"))
                .andExpect(status().isForbidden());
    }


    
}
