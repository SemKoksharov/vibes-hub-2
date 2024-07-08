package dev.semkoksharov.vibeshub2.dto.user;

public class AdvertiserDTO {

    private Long userId;
    private String description;
    private String company;
    private String taxCode;

    public AdvertiserDTO() {
    }

    public AdvertiserDTO(Long userId, String description, String company, String taxCode) {
        this.userId = userId;
        this.description = description;
        this.company = company;
        this.taxCode = taxCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }
}
