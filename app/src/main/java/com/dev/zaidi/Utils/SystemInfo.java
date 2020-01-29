package com.dev.zaidi.Utils;

/**
 * Created by Eric on 1/9/2018.
 */

public class SystemInfo {
    String board;
    String bootloader;
    String brand;
    String cpu_abi;
    String cpu_ab12;
    String device;
    String diaplay;
    String display;
    String build;
    String host;
    String buildid;
    //String isDebuggableIS ;
    String manufacturer;
    String model;
    String product;
    String radio;
    String serial;
    //String  Build.SUPPORTED_32_BIT_ABIS = [Ljava.lang.String;@3dd90541
    // Build.SUPPORTED_64_BIT_ABIS = [Ljava.lang.String;@1da4fc3
    //  Build.SUPPORTED_ABIS = [Ljava.lang.String;@525f635
    //  Build.TAGS = release-keys
    String time;
    String type;
    //  Build.UNKNOWN = unknown
    String user;
    String fingerprint;

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getBootloader() {
        return bootloader;
    }

    public void setBootloader(String bootloader) {
        this.bootloader = bootloader;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCpu_abi() {
        return cpu_abi;
    }

    public void setCpu_abi(String cpu_abi) {
        this.cpu_abi = cpu_abi;
    }

    public String getCpu_ab12() {
        return cpu_ab12;
    }

    public void setCpu_ab12(String cpu_ab12) {
        this.cpu_ab12 = cpu_ab12;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDiaplay() {
        return diaplay;
    }

    public void setDiaplay(String diaplay) {
        this.diaplay = diaplay;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBuildid() {
        return buildid;
    }

    public void setBuildid(String buildid) {
        this.buildid = buildid;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getRadio() {
        return radio;
    }

    public void setRadio(String radio) {
        this.radio = radio;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
