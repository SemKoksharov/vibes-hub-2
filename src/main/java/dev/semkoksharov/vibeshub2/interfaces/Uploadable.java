package dev.semkoksharov.vibeshub2.interfaces;

public interface Uploadable {

    Long getId();

    String getMinioPath();

    void setMinioPath(String minioPath);

    void setDirectUrl(String directUrl);

    String getDirectUrl();
}
