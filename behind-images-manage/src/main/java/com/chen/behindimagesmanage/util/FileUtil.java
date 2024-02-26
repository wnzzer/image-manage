package com.chen.behindimagesmanage.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author 15031
 */
public class FileUtil {
    public  static String IMG_FOLDER_PATH = Paths.get("/app/images").toAbsolutePath().toString();

    /**
     * 获取文件名和后缀
     * @param filePath 完整文件名
     * @return 返回前缀和后缀
     */
    public static String[] splitFileNameAndExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        int lastDotIndex = filePath.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
            String fileName = filePath.substring(0, lastDotIndex);
            String fileExtension = filePath.substring(lastDotIndex + 1);
            return new String[] { fileName, fileExtension };
        } else {
            return null;
        }
    }

    public static String getFileMd5(MultipartFile file) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        digest.update(bytes);
        byte[] md5sum = digest.digest();

        // 将 MD5 散列值转换为十六进制字符串
        StringBuilder hexBuilder = new StringBuilder();
        for (byte b : md5sum) {
            hexBuilder.append(String.format("%02x", b));
        }

        return hexBuilder.toString();
    }
    public static void makerDir(String directoryPath){
        Path directory = Paths.get(directoryPath);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("文件夹创建成功");
            } catch (IOException e) {
                System.err.println("文件夹创建失败: " + e.getMessage());
            }
        } else {
            System.out.println("文件夹已存在");
        }
    }

    /**
     * 打包文件夹所有文件
     * @param sourceDirectory 目标文件夹
     * @param zipFileName zip名
     * @return zip文件对象
     * @throws IOException io异常
     */
    public static File zipDirectory(String sourceDirectory, String zipFileName) throws IOException {
        File sourceDir = new File(sourceDirectory);
        File zipFile = new File(zipFileName);

        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zip(sourceDir, sourceDir.getName(), zos);
        }

        return zipFile;
    }

    private static void zip(File directory, String parent, ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[1024];

        for (File file : files) {
            if (file.isDirectory()) {
                zip(file, parent + File.separator + file.getName(), zos);
                continue;
            }

            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(parent + File.separator + file.getName());
            zos.putNextEntry(zipEntry);

            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }

            fis.close();
        }
    }

    /**
     * 把文件读为字节流
     * @param file 文件对象
     * @return 字节流
     * @throws IOException io异常
     */
    public static byte[] readFileToByteArray(File file) throws IOException {
        try (InputStream is = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }

    /**
     * 读取所有文件名
     * @param directoryPath
     * @return
     */
    public static List<String> listFilesInDirectory(String directoryPath) {
        List<String> fileNames = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        fileNames.add(file.getName());
                    }
                }
            }
        }

        return fileNames;
    }


    public static void unZip(byte[] fileData,String targetDirectory){
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                // 只处理文件条目，跳过目录条目
                if (!entry.isDirectory()) {
                    String entryName = entry.getName();
                    File entryFile = new File(targetDirectory, entryName);

                    try (FileOutputStream fileOutputStream = new FileOutputStream(entryFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  static String countFileSizetoString(long fileSizeInBytes){
        int unitBase = 1024;
        String fileSize = null;
        if (fileSizeInBytes < unitBase) {
            fileSize = String.format("%d Bytes", fileSizeInBytes);
        } else if (fileSizeInBytes < (long) unitBase * unitBase) {
            fileSize = String.format("%.2f KB", (double) fileSizeInBytes / unitBase);
        } else {
            fileSize = String.format("%.2f MB", (double) fileSizeInBytes / (unitBase * unitBase));
        }
        return fileSize;
    }
    public static byte[]  getThumbnailBaos(MultipartFile image){
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 创建缩略图并保存到字节数组
        ByteArrayOutputStream thumbnailBaos = new ByteArrayOutputStream();
        try {
            Thumbnails.of(originalImage)
                    .size(100, 100)
                    .crop(Positions.CENTER)
                    .outputFormat("jpeg")
                    .toOutputStream(thumbnailBaos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return thumbnailBaos.toByteArray();
    }


    public enum SizeUnit {
        KB,
        MB,
        GB
    }

    /**
     * Checks if the file size in bytes is less than the specified limit in the given unit.
     *
     * @param fileSizeInBytes the size of the file in bytes
     * @param limit the limit to compare the file size with
     * @param unit the unit of the limit (KB, MB, GB)
     * @return true if fileSizeInBytes is less than the limit in the specified unit, false otherwise
     */
    public static boolean isFileSizeLessThan(long fileSizeInBytes, double limit, SizeUnit unit) {
        double sizeInUnit;
        switch (unit) {
            case KB:
                sizeInUnit = fileSizeInBytes / 1024.0;
                break;
            case MB:
                sizeInUnit = fileSizeInBytes / (1024.0 * 1024.0);
                break;
            case GB:
                sizeInUnit = fileSizeInBytes / (1024.0 * 1024.0 * 1024.0);
                break;
            default:
                throw new IllegalArgumentException("Invalid size unit");
        }
        return sizeInUnit < limit;
    }


}
