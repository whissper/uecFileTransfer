package org.me.uecfile;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.MTOM;

/**
 * file transfer web-service
 * @author SAV2
 * @version 1.0.1
 * @since 21.04.2015 23:44
 */
@WebService(serviceName = "uecFile")
@MTOM(enabled = true)
public class uecFile
{
    @Resource
    WebServiceContext wsc;
    
    //own constructor
    public uecFile(){}
    
    //check for authentification
    private boolean isAuth()
    {
        MessageContext mc = wsc.getMessageContext();
        Map requestHeader = (Map) mc.get(MessageContext.HTTP_REQUEST_HEADERS);
        List userList = (List) requestHeader.get("Username");
        List passList = (List) requestHeader.get("Password");
        String username = "";
        String password = "";
        if(userList != null && passList != null)
        {
            username = (String)userList.get(0);
            password = (String)passList.get(0);
        }
        return ("userasadmin".equals(username) && "GelVjkJxfGbz".equals(password));
    }
    
    /**
     * Download File method
     * @param fileName : <code>String</code> -IN
     * @param resultSuccessVal : <code>String</code> -OUT
     * @param resultDescriptionVal : <code>String</code> -OUT
     * @param downloadedFileVal : <code>byte[]</code> -OUT 
     */
    @WebMethod(operationName = "downloadFile")
    //@WebResult(name = "XMLReturnMessage")
    public void downloadFileFromServer(@WebParam(name = "fileName", mode = WebParam.Mode.IN) String fileName,
                                       @WebParam(name = "resultSuccess", mode = WebParam.Mode.OUT) Holder<String> resultSuccessVal,
                                       @WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescriptionVal,
                                       @WebParam(name = "downloadedFile", mode = WebParam.Mode.OUT) Holder<byte[]> downloadedFileVal)
    {
        if(!isAuth())
        {
            resultSuccessVal.value = "false";
            resultDescriptionVal.value = "authentication error";
            downloadedFileVal.value = null;
            return;
        }
        
        FileTransferContainer fileTransfCont = new FileTransferContainer();
        
        downloadedFileVal.value = fileTransfCont.downloadFile(fileName);
        resultSuccessVal.value = fileTransfCont.resultSuccess;
        resultDescriptionVal.value = fileTransfCont.resultDescription;
        
        fileTransfCont = null;
    }
    
    /**
     * Upload File method
     * @param fileBytesVal : <code>byte[]</code> -IN
     * @param fileExtensionVal : <code>String</code> -IN
     * @param resultSuccessVal : <code>String</code> -OUT
     * @param resultDescriptionVal : <code>String</code> -OUT
     * @param fileNameVal : <code>String</code> -OUT
     */
    @WebMethod(operationName = "uploadFile")
    //@WebResult(name = "XMLReturnMessage")
    public void uploadFileToServer(@WebParam(name = "fileBytes", mode = WebParam.Mode.IN) byte[] fileBytesVal,
                                   @WebParam(name = "fileExtension", mode = WebParam.Mode.IN) String fileExtensionVal,
                                   @WebParam(name = "resultSuccess", mode = WebParam.Mode.OUT) Holder<String> resultSuccessVal,
                                   @WebParam(name = "resultDescription", mode = WebParam.Mode.OUT) Holder<String> resultDescriptionVal,
                                   @WebParam(name = "fileName", mode = WebParam.Mode.OUT) Holder<String> fileNameVal)
    {
        if(!isAuth())
        {
            resultSuccessVal.value = "false";
            resultDescriptionVal.value = "authentication error";
            fileNameVal.value = "";
            return;
        }
        
        FileTransferContainer fileTransfCont = new FileTransferContainer();
        
        fileTransfCont.uploadFile(fileBytesVal, fileExtensionVal);
        resultSuccessVal.value = fileTransfCont.resultSuccess;
        resultDescriptionVal.value = fileTransfCont.resultDescription;
        fileNameVal.value = fileTransfCont.uploadedFileName;
        
        fileTransfCont = null;
    }
}
