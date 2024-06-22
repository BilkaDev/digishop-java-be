package pl.networkmanager.bilka.ftpserver.service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.networkmanager.bilka.ftpserver.entity.Image;
import pl.networkmanager.bilka.ftpserver.exceptions.FtpConnectionException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Service
public class FtpService {
    @Value("${ftp.server}")
    private String FTP_SERVER;
    @Value("${ftp.username}")
    private String FTP_USERNAME;
    @Value("${ftp.password}")
    private String FTP_PASSWORD;
    @Value("${ftp.origin}")
    private String FTP_ORIGIN_DIRECTORY;
    @Value("${ftp.port}")
    private int FTP_PORT;


    private FTPClient getFtpConnection() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(FTP_SERVER, FTP_PORT);
        ftpClient.login(FTP_USERNAME, FTP_PASSWORD);

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        return ftpClient;
    }

    public Image uploadFileToFtp(MultipartFile file) throws FtpConnectionException, IOException {
        try {
            FTPClient ftpClient = getFtpConnection();
            String remoteFilePath = FTP_ORIGIN_DIRECTORY + "/" + LocalDate.now() + "/" + file.getOriginalFilename();
            boolean uploaded = streamFile(file, ftpClient, remoteFilePath);
            if (!uploaded) {
                createFolder(ftpClient);
                if (!streamFile(file, ftpClient, remoteFilePath)) {
                    throw new FtpConnectionException("Cannot connect to server");
                }
            }
            ftpClient.logout();
            ftpClient.disconnect();
            return Image.builder()
                    .path(remoteFilePath)
                    .build();
        } catch (IOException e) {
            throw new FtpConnectionException(e);
        }
    }

    private void createFolder(FTPClient client) throws IOException {
        client.makeDirectory(FTP_ORIGIN_DIRECTORY + "/" + LocalDate.now());
    }

    private boolean streamFile(MultipartFile file, FTPClient ftpClient, String remoteFilePath) throws IOException {
        InputStream inputStream = file.getInputStream();
        boolean uploaded = ftpClient.storeFile(remoteFilePath, inputStream);
        inputStream.close();
        return uploaded;
    }

    public boolean deleteFile(String path) throws IOException {
        FTPClient ftpClient = getFtpConnection();
        boolean deleted = ftpClient.deleteFile(path);
        ftpClient.logout();
        ftpClient.disconnect();
        return deleted;
    }

    public ByteArrayOutputStream getFile(Image image) throws IOException {
        FTPClient ftpClient = getFtpConnection();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        boolean downloaded = ftpClient.retrieveFile(image.getPath(), outputStream);
        ftpClient.logout();
        ftpClient.disconnect();
        if (downloaded) {
            return outputStream;
        }
        throw new FtpConnectionException("Cannot download file");
    }
}
