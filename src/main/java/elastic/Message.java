package elastic;

import java.util.Date;

public class Message {
    private String id;
    private String clientIP;
    private Date sentTime;
    private String uuid;
    private String requestURL;
    private String responseCode;
    private Integer fileSize;
    private String client_location;
    private String browser;

    public Message() {
    }

    public Message(String id, String clientIP, Date sentTime, String uuid, String requestURL, String responseCode, Integer fileSize, String client_location, String browser) {
        this.id = id;
        this.clientIP = clientIP;
        this.sentTime = sentTime;
        this.uuid = uuid;
        this.requestURL = requestURL;
        this.responseCode = responseCode;
        this.fileSize = fileSize;
        this.client_location = client_location;
        this.browser = browser;
    }

    public String getClient_location() {
        return client_location;
    }

    public void setClient_location(String client_location) {
        this.client_location = client_location;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requsetURL) {
        this.requestURL = requsetURL;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }
}
