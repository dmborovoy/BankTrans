package com.mt940.rest;


import com.mt940.rest.configuration.TestConfig;
import com.mt940.rest.controllers.TransactionController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
@Sql({"classpath:sql/V2_test-data.sql"})
public class RestTransactionTest {
    @Autowired
    private WebApplicationContext wac;


//    @Autowired
//    TransactionController transactionController;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        //this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesTransactionController() {
        ServletContext servletContext = wac.getServletContext();

        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(wac.getBean(TransactionController.class));
    }

    @Test
    public void givenURIWithPathVariable_whenMockMVC_thenResponse_NotFound() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isNotFound());
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.message").value("No message available"));
    }

    @Test
    public void givenURIWithPathVariableTransactionID15_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc.perform(get("/transaction/15"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value("15"))
                .andExpect(jsonPath("date").value("2014-01-19T00:00:00+11:00"))
                .andExpect(jsonPath("entryDate").value("2014-01-19T00:00:00+11:00"))
                .andExpect(jsonPath("fundsCode").value("CREDIT"))
                .andExpect(jsonPath("amount").value("3000.0"))
                .andExpect(jsonPath("currency").value("USD"))
                .andExpect(jsonPath("swiftCode").value("NTRF"))
                .andExpect(jsonPath("referenceForAccountOwner").value("BLING CITY LIMIT"))
                .andExpect(jsonPath("referenceForBank").value(""))
                .andExpect(jsonPath("statementId").value("2"))
                .andExpect(jsonPath("status").value("NEW"))
                .andExpect(jsonPath("errorDescription").isEmpty())
                .andExpect(jsonPath("instance").value("RUSSIA"))
                .andExpect(jsonPath("entryOrder").value("1"))
                .andExpect(jsonPath("informationToAccountOwner").value("MERCHANT ACCOUNT 000000106623\\n"));
    }

    @Test
    public void givenURIWithPathVariableDelete_whenMockMVC_thenResponseOK() throws Exception {
        this.mockMvc.perform(delete("/transaction/21"))
                .andDo(print()).andExpect(status().isOk());
    }


}
