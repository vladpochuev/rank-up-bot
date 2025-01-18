package com.luxusxc.rank_up.web.controllers;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
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

import java.util.List;
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
    @MockBean
    private DefaultRankRepository defaultRankRepository;
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
        List<DefaultRankEntity> defaultRanks = List.of(
                new DefaultRankEntity(1, "HERALD", 10L),
                new DefaultRankEntity(1, "GUARDIAN", 20L));
        when(defaultRankRepository.findAll()).thenReturn(defaultRanks);

        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableAll(true);
        webConfig.setEnableCustomLevels(true);
        webConfig.setCustomLevels("12, 23");
        webConfig.setEnableCustomRanks(false);
        webConfig.setLevelUpMessage("Congratulations!");
        webConfig.setAttachedImagesUrl("https://i.imgur.com/ZmcYXQK.jpeg");

        mockMvc.perform(post("/")
                        .param("enableAll", "true")
                        .param("enableCustomLevels", "true")
                        .param("customLevels", "12, 23")
                        .param("enableCustomRanks", "false")
                        .param("levelUpMessage", "Congratulations!")
                        .param("attachedImagesUrl", "https://i.imgur.com/ZmcYXQK.jpeg"))
                .andExpect(status().is3xxRedirection());

        ArgumentCaptor<WebRankUpConfig> captor = ArgumentCaptor.forClass(WebRankUpConfig.class);
        verify(configurer, times(1)).importWebConfig(captor.capture());

        assertThat(captor.getValue(), equalTo(webConfig));
    }
}