package com.luxusxc.rank_up.web.controllers;

import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.service.WebRankUpConfigurer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MainControllerTest {
    @MockBean
    private WebRankUpConfigurer configurer;
    private final MockMvc mockMvc;

    public MainControllerTest(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGet() throws Exception {
        when(configurer.exportWebConfig()).thenReturn(new WebRankUpConfig());

        mockMvc
                .perform(get(""))
                .andExpect(status().isOk());
    }

    @Test
    void testGetHtml() throws Exception {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableAll(true);
        webConfig.setCustomRanks("TEST1, TEST2");
        when(configurer.exportWebConfig()).thenReturn(webConfig);

        String html = mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Document document = Jsoup.parse(html);

        Element checkBox = document.selectFirst(".enableAll");
        String isChecked = checkBox.attr("value");
        assertTrue(Boolean.parseBoolean(isChecked));

        Element textBox = document.selectFirst("#ranks_textarea");
        Map<String, String> dataset = textBox.dataset();
        assertThat(dataset.get("custom"), equalTo("TEST1, TEST2"));
    }

    @Test
    void testPost() throws Exception {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableAll(true);
        webConfig.setCustomLevels("12, 23");

        mockMvc.perform(post("/")
                        .param("customLevels", "12, 23")
                        .param("enableAll", "true"))
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<WebRankUpConfig> captor = ArgumentCaptor.forClass(WebRankUpConfig.class);
        verify(configurer, times(1)).importWebConfig(captor.capture());

        assertThat(captor.getValue(), equalTo(webConfig));
    }
}