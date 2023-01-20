package net.ishop.services.impl;

import net.framework.annotations.dependency_injection.Component;
import net.framework.annotations.dependency_injection.Value;
import net.ishop.exceptions.InternalServerErrorException;
import net.ishop.services.interfaces.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileStorageAvatarServiceImpl implements AvatarService {
    @Value(value = "app.avatar.root.dir")
    private String rootDir;

    @Override
    public String processGettingAvatarLink(String avatarUrlFromSocialNetwork) {
        try {
            String uniqFileName = generateUniqFileName();
            Path pathFileToSave = Paths.get(rootDir + "/" + uniqFileName);
            downloadAvatar(avatarUrlFromSocialNetwork, pathFileToSave);
            return "/media/avatars/" + uniqFileName;
        } catch (IOException e) {
            throw new InternalServerErrorException("Can't process avatar link", e);
        }
    }

    protected String generateUniqFileName() {
        return UUID.randomUUID().toString() + ".jpg";
    }

    protected void downloadAvatar(String avatarUrl, Path pathFileToSave) throws IOException {
        try (InputStream inputStream = new URL(avatarUrl).openStream()) {
            Files.copy(inputStream, pathFileToSave);
        }
    }
}
