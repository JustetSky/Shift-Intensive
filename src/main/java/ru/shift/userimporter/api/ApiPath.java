package ru.shift.userimporter.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPath {
    public static final String CLIENTS = "/clients";
    public static final String FILES = "/files";
    public static final String FILE_ID = "/{fileId}";
    public static final String PROCESSING = "/processing";
    public static final String STATISTICS = "/statistics";
    
}
