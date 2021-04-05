package com.example.nalone.objects;

import java.sql.Date;

public class UserInvitation extends User{

    private Date invitationDate;

    public UserInvitation(String uid, String last_name, String first_name, String sex, String city,
                String number, String mail,
                String description, String birthday_date, Date date){
        super(uid,last_name,first_name,sex,city,number,mail,description,birthday_date);
        this.invitationDate = date;
    }

    public Date getInvitationDate() {
        return invitationDate;
    }

    public void setInvitationDate(Date invitationDate) {
        this.invitationDate = invitationDate;
    }
}
