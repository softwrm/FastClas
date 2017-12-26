package com.versatile.fastclas.models;

/**
 * Created by USER on 13-12-2017.
 */

public class SessionsModel {

    public String sessionId, itemsViewed, itemCount, status, sessionTitle, sessionNumber;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getItemsViewed() {
        return itemsViewed;
    }

    public void setItemsViewed(String itemsViewed) {
        this.itemsViewed = itemsViewed;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSessionTitle() {
        return sessionTitle;
    }

    public void setSessionTitle(String sessionTitle) {
        this.sessionTitle = sessionTitle;
    }

    public String getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }
}
