package me.NickP0is0n.jTestStudent.models;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskSet extends ArrayList<Task> implements Serializable {
    private boolean passwordProtected;
    private String password;

    public void setPassword(String password) {
        this.password = password;
        this.passwordProtected = true;
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public void setPasswordProtected(boolean passwordProtected) {
        this.passwordProtected = passwordProtected;
    }

    public String getPassword() {
        return password;
    }


}
