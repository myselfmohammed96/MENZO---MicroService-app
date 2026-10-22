package com.menzo.User_Service.Onboarding.Service;

import com.menzo.User_Service.Global.Config.StorageProperties;
import com.menzo.User_Service.Global.Exception.ErrorCode;
import com.menzo.User_Service.Global.Exception.FileStorageException;
import com.menzo.User_Service.Global.Exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

    private final StorageProperties properties;
    private final Path root;

    public FileStorageService(StorageProperties properties) {
        this.properties = properties;
        this.root = properties.profileDir().toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create upload directory " + root, e);
        }
    }

    public String storeProfilePicture(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;   // browsers send an empty part when no file is chosen -> treat as "not provided"
        }
        if (file.getSize() > properties.maxProfilePicSize().toBytes()) {
            throw new InvalidFileException(ErrorCode.FILE_TOO_LARGE,
                    "Profile picture must be at most " + properties.maxProfilePicSize().toMegabytes() + " MB.");
        }
        String declaredType = file.getContentType();
        if (declaredType == null || !ALLOWED_TYPES.contains(declaredType.toLowerCase(Locale.ROOT))) {
            throw new InvalidFileException(ErrorCode.INVALID_PROFILE_PICTURE,
                    "Profile picture must be a JPEG, PNG or WebP image.");
        }

        String extension;
        try (InputStream in = file.getInputStream()) {
            extension = detectExtension(in.readNBytes(12));   // trust the bytes, not the client's headers
        } catch (IOException e) {
            throw new FileStorageException("Could not read the uploaded profile picture", e);
        }
        if (extension == null) {
            throw new InvalidFileException(ErrorCode.INVALID_PROFILE_PICTURE,
                    "Profile picture is not a valid JPEG, PNG or WebP image.");
        }

        String filename = UUID.randomUUID() + "." + extension;   // never use the client's file name
        Path target = root.resolve(filename).normalize();
        if (!root.equals(target.getParent())) {
            throw new FileStorageException("Resolved upload path escaped the upload directory", null);
        }
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target);
        } catch (IOException e) {
            throw new FileStorageException("Could not store the profile picture", e);
        }
        log.info("Profile picture stored as {}", filename);
        return properties.publicUrlPrefix() + "/" + filename;
    }

    public void deleteQuietly(String publicUrl) {
        if (publicUrl == null) {
            return;
        }
        try {
            String filename = publicUrl.substring(publicUrl.lastIndexOf('/') + 1);
            Path target = root.resolve(filename).normalize();
            if (root.equals(target.getParent())) {
                Files.deleteIfExists(target);
                log.info("Removed orphaned profile picture {}", filename);
            }
        } catch (IOException | RuntimeException e) {
            log.warn("Could not remove orphaned profile picture {}", publicUrl, e);
        }
    }

    private String detectExtension(byte[] h) {
        if (h.length >= 3 && (h[0] & 0xFF) == 0xFF && (h[1] & 0xFF) == 0xD8 && (h[2] & 0xFF) == 0xFF) {
            return "jpg";
        }
        if (h.length >= 8 && (h[0] & 0xFF) == 0x89 && h[1] == 'P' && h[2] == 'N' && h[3] == 'G'
                && h[4] == 0x0D && h[5] == 0x0A && h[6] == 0x1A && h[7] == 0x0A) {
            return "png";
        }
        if (h.length >= 12 && h[0] == 'R' && h[1] == 'I' && h[2] == 'F' && h[3] == 'F'
                && h[8] == 'W' && h[9] == 'E' && h[10] == 'B' && h[11] == 'P') {
            return "webp";
        }
        return null;
    }
}
