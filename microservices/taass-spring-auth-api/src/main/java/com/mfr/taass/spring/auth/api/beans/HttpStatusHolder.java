package com.mfr.taass.spring.auth.api.beans;

import java.io.Serializable;

public interface HttpStatusHolder extends Serializable {
    public int getStatus();
    public void setStatus(int status);
}
