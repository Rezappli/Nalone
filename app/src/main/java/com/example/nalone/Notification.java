package com.example.nalone;

public class Notification {

    public Notification(TypeNotification type){
        switch (type){
            case JOIN_AMI:joinAmi();
                break;
            case JOIN_EVENT:joinEvent();
                break;
            case DEMANDE_AMI:demandeAmi();
                break;
            case JOIN_GROUPE:joinGroupe();
                break;
            case DEMANDE_EVENT:demandeEvent();
                break;
            case DEMANDE_GROUPE:demandeGroupe();
                break;
        }
    }

    private void demandeGroupe() {

    }

    private void demandeEvent() {

    }

    private void joinGroupe() {
    }

    private void demandeAmi() {
    }

    private void joinEvent() {
    }

    private void joinAmi(){

    }
}
