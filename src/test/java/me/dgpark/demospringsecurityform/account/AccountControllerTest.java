package me.dgpark.demospringsecurityform.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Test
    public void index() throws Exception{
        // given
        String url = "/";
        
        // when
        mockMvc.perform(get(url).with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
        
        // then
    }


    @Test
    public void index_user() throws Exception{
        // given
        String url = "/";

        // when
        mockMvc.perform(get(url).with(user("dgpark").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());

        // then
    }

    @Test
    public void admin_user() throws Exception{
        // given
        String url = "/admin";

        // when
        mockMvc.perform(get(url).with(user("dgpark").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());

        // then
    }

    @Test
    public void admin_admin() throws Exception{
        // given
        String url = "/admin";
        // when
        mockMvc.perform(get(url).with(user("dgpark").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());

        // then
    }


    @Test
    @Transactional
    public void loginSuccess() throws Exception {
        //given
        String username = "dgpark";
        String password = "123";
        createUser(username, password);

        //when
        mockMvc.perform(formLogin().user(username).password(password))
                .andExpect(authenticated());
    }

    @Test
    @Transactional
    public void loginFail() throws Exception {
        //given
        String username = "dgpark";
        String password = "123";
        createUser(username, password);

        //when
        mockMvc.perform(formLogin().user(username).password("12345"))
                .andExpect(unauthenticated());
    }

    private void createUser(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole("USER");
        accountService.createNew(account);
    }

}