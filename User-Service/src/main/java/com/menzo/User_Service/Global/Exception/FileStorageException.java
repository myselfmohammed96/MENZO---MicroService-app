package com.menzo.User_Service.Global.Exception;

public class FileStorageException extends BusinessException {

    public FileStorageException(String message, Throwable cause) {
        super(ErrorCode.FILE_STORAGE_FAILED, message, cause);
    }
}
