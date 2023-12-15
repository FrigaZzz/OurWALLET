package com.mfr.taass.spring.transaction.api.beans;

import java.io.Serializable;

public interface HttpStatusHolder extends Serializable {
    public int getStatus();
    public void setStatus(int status);
}
