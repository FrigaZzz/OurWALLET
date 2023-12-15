package com.mfr.taass.spring.auth.api.beans;

import java.io.Serializable;

public class BaseResponse<M extends HttpStatusHolder, B> implements Serializable {
    private M meta;
    private B body;

    public BaseResponse(M meta, B body) {
        this.meta = meta;
        this.body = body;
    }

    public BaseResponse(M meta) {
        this.meta = meta;
        this.body = null;
    }

    public M getMeta() {
        return meta;
    }

    public void setMeta(M meta) {
        this.meta = meta;
    }

    public B getBody() {
        return body;
    }

    public void setBody(B body) {
        this.body = body;
    }
}
