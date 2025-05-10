package com.yyz.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class GreaseConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public final int fire_tick = 160;
    public final int poison_tick = 400;
    public final int freezing_tick = 160;
    public final int lightning_cooldown = 100;
    public final int grease_tick = 600;
    public final int sacred_damage = 2;
    public final int magic_change = 100;

    public static GreaseConfig loadConfig(File file) {
        GreaseConfig config;

        if (file.exists() && file.isFile()) {
            try (
                    FileInputStream fileInputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ) {
                config = GSON.fromJson(bufferedReader, GreaseConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config", e);
            }
        } else {
            config = new GreaseConfig();
        }

        config.saveConfig(file);

        return config;
    }

    public void saveConfig(File config) {
        try (
                FileOutputStream stream = new FileOutputStream(config);
                Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
        ) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }



}
