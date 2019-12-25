package me.dgpark.demospringsecurityform.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerWithAnnotionTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    public void index_ann() throws Exception{
        // given
        String url = "/";

        // when
        mockMvc.perform(get(url).with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());

        // then
    }

    @Test
    @WithMockUser(username = "dgpark", roles = "USER")
    public void index_user_ann() throws Exception{
        // given
        String url = "/";

        // when
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk());

        // then
    }

    @Test
    @WithMockUser(username = "dgpark", roles = "USER")
    public void admin_user_ann() throws Exception{
        // given
        String url = "/admin";
        // when
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
    }

    @Test
    @WithMockUser(username = "dgpark", roles = "ADMIN")
    public void admin_admin_ann() throws Exception{
        // given
        String url = "/admin";
        // when
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk());

        // then
    }
}