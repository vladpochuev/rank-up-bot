package com.luxusxc.rank_up;

import com.luxusxc.rank_up.repository.ImageRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import com.luxusxc.rank_up.service.RankUpConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;

@SpringBootApplication
public class RankUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(RankUpApplication.class, args);
    }

    @Bean
    public RankUpConfig rankUpConfig(RankRepository rankRepository, ImageRepository imageRepository) {
        try(FileInputStream fis = new FileInputStream("src/main/resources/rankUpConfig.ser");
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            RankUpConfig config = (RankUpConfig) ois.readObject();
            config.setRankRepository(rankRepository);
            config.setImageRepository(imageRepository);
            return config;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
