package com.jhzy.nursinghandover.beans;

/**
 * Created by sxmd on 2017/3/6.
 */

public class NFCBean {
    private String nfc;
    private String fun;

    public NFCBean(String nfc, String fun) {
        this.nfc = nfc;
        this.fun = fun;
    }

    public String getFun() {
        return fun;
    }

    public String getNfc() {
        return nfc;
    }

    public void setNfc(String nfc) {
        this.nfc = nfc;
    }
}
