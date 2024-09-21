package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class RankUpConfigHandlerTest {
    private RankUpConfigHandler configHandler;

    @BeforeEach
    void getConfig() {
        configHandler = new RankUpConfigHandler();
    }

    @Test
    void testImportConfig() {
        String tmpConfigPath = "src/test/java/com/luxusxc/rank_up/rankUpConfig.ser";
        configHandler.setConfigPath(tmpConfigPath);

        RankUpConfig config = new RankUpConfig();
        configHandler.setConfig(config);
        configHandler.importConfig();

        File file = new File(tmpConfigPath);
        assertTrue(file.exists());

        RankUpConfig desConfig = deserializeConfig(file);
        assertThat(config, equalTo(desConfig));

        assertTrue(file.delete());
    }

    @Test
    void testExportConfig() {
        RankUpConfig config = configHandler.getConfig();
        File file = new File("src/main/resources/rankUpConfig.ser");
        RankUpConfig desConfig = deserializeConfig(file);
        assertThat(config, equalTo(desConfig));
    }

    @Test
    void testExportFileDoNotExists() {
        String randomPath = "src/VU1JtD/rankUpConfig.ser";
        File randomFile = new File(randomPath);
        assertFalse(randomFile.exists());

        configHandler.setConfigPath(randomPath);
        configHandler.exportConfig();
        assertThat(configHandler.getConfig(), equalTo(new RankUpConfig()));
    }

    @Test
    void testImportInvalidPath() {
        String invalidPath = "";
        File invalidFile = new File(invalidPath);
        assertFalse(invalidFile.exists());
        configHandler.setConfigPath(invalidPath);
        assertThrows(RuntimeException.class, () -> configHandler.importConfig());
    }

    private RankUpConfig deserializeConfig(File file) {
        try (InputStream stream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(stream)) {
            return (RankUpConfig) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFillFromWebConfig() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setEnableAll(true);
        webConfig.setEnableCustomRanks(false);
        webConfig.setEnableCustomLevels(true);
        webConfig.setAnnounceLevelUp(false);
        webConfig.setLevelUpMessage("name: {name}");
        configHandler.fillFromWebConfig(webConfig);

        RankUpConfig config = configHandler.getConfig();
        assertThat(config.isEnableAll(), equalTo(webConfig.isEnableAll()));
        assertThat(config.isEnableCustomRanks(), equalTo(webConfig.isEnableCustomRanks()));
        assertThat(config.isEnableCustomLevels(), equalTo(webConfig.isEnableCustomLevels()));
        assertThat(config.isAnnounceLevelUp(), equalTo(webConfig.isAnnounceLevelUp()));
        assertThat(config.getLevelUpMessageFormat(), equalTo(webConfig.getLevelUpMessage()));
    }

    @Test
    void testFillFromWebConfigNull() {
        assertThrows(NullPointerException.class, () -> configHandler.fillFromWebConfig(null));
    }

    @Test
    void testFillFromWebConfigNullFields() {
        WebRankUpConfig webConfig = new WebRankUpConfig();
        webConfig.setLevelUpMessage(null);
        configHandler.fillFromWebConfig(webConfig);

        RankUpConfig config = configHandler.getConfig();
        assertThat(config.getLevelUpMessageFormat(), nullValue());
    }
}
