package com.ozangunalp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAgent {
    public String userAgentString;
    public String name;
    public String type;
    public String version;
    public String versionMajor;
    public Device device;
    public Engine engine;
    public OperatingSystem operatingSystem;

    public static class Device {
        public String name;
        public String type;
        public String brand;
        @JsonProperty("CPU")
        public String cpu;
    }

    public static class Engine {
        public String name;
        public String type;
        public String version;
        public String versionMajor;
    }

    public static class OperatingSystem {
        public String name;
        public String type;
        public String version;
        public String versionMajor;
    }
}