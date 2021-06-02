package com.nolonely.mobile.objects;

public class UserInvitation {

    private String uid;
    private String requestDate;

    public UserInvitation(String uid, String date) {
        this.uid = uid;
        this.requestDate = date;
    }

    public String setRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String invitationDate) {
        this.requestDate = invitationDate;
    }
}
