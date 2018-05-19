package org.me.uecfile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * File Transfer Container
 * @author SAV2
 * @version 1.0.0
 * @since 20.04.2015 12:44
 */
public class FileTransferContainer 
{
    private static final String DIRECTORY_PATH = "/var/sfiles/";
    
    public String resultSuccess = "";
    public String resultDescription = "";
    public String uploadedFileName = "";
    
    //own Constructor
    public FileTransferContainer(){}
    
    //download
    public byte[] downloadFile(String fileName)
    {
        String filePath = DIRECTORY_PATH + fileName;
        
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream inputStream = new BufferedInputStream(fis);
            byte[] fileBytes = new byte[(int) file.length()];
            inputStream.read(fileBytes);
            inputStream.close();
            
            resultSuccess = "true";
            resultDescription = "file was successfully downloaded";
            return fileBytes;
        }
        catch(FileNotFoundException ex)
        {
            resultSuccess = "false";
            resultDescription = "Got FileNotFoundException, the reason is: " + ex;
            return null;
        }
        catch(IOException ex)
        {
            resultSuccess = "false";
            resultDescription = "Got IOException, the reason is: " + ex;
            return null;
        }
    }
    
    //upload
    public void uploadFile(byte[] fileBytes, String extension)
    {
        String fileName = RandomStringUtils.randomAlphanumeric(6).toUpperCase() + System.currentTimeMillis() + "." + extension;
        
        String filePath = DIRECTORY_PATH + fileName;
        
        try
        {
            FileOutputStream fos = new FileOutputStream(filePath);
            BufferedOutputStream outputStream = new BufferedOutputStream(fos);
            outputStream.write(fileBytes);
            outputStream.close();
            
            resultSuccess = "true";
            resultDescription = "file was successfully uploaded";
            uploadedFileName = fileName;
        }
        catch(FileNotFoundException ex)
        {
            resultSuccess = "false";
            resultDescription = "Got FileNotFoundException, the reason is: " + ex;
        }
        catch(IOException ex)
        {
            resultSuccess = "false";
            resultDescription = "Got IOException, the reason is: " + ex;
        }
    }
}
