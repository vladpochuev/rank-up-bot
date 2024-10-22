package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.mapper.WebRankUpConfigMapper;
import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Getter
@Setter
public class RankUpConfigHandler {
    private WebRankUpConfigMapper webRankUpConfigMapper;
    private String configPath = "src/main/resources/rankUpConfig.ser";
    private RankUpConfig config;

    public RankUpConfigHandler(WebRankUpConfigMapper webRankUpConfigMapper) {
        this.webRankUpConfigMapper = webRankUpConfigMapper;
        exportConfig();
    }

    public void importConfig() {
        try (FileOutputStream fileOut = new FileOutputStream(configPath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportConfig() {
        try (FileInputStream fis = new FileInputStream(configPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            config = (RankUpConfig) ois.readObject();
        } catch (FileNotFoundException e) {
            config = new RankUpConfig();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillFromWebConfig(WebRankUpConfig webConfig) {
        if (webConfig == null) {
            throw new NullPointerException();
        }
        config = webRankUpConfigMapper.toRankUpConfig(webConfig);
    }
}
