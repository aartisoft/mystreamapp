
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Company_ {

    @SerializedName("CompanyId")
    @Expose
    private Integer companyId;
    @SerializedName("ParentCompanyId")
    @Expose
    private Integer parentCompanyId;
    @SerializedName("CompanyName")
    @Expose
    private String companyName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("Faxnumber")
    @Expose
    private String faxnumber;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("WebsiteUrl")
    @Expose
    private String websiteUrl;
    @SerializedName("LanguageCode")
    @Expose
    private String languageCode;
    @SerializedName("DateFormat")
    @Expose
    private Object dateFormat;
    @SerializedName("TimeZone")
    @Expose
    private Object timeZone;
    @SerializedName("IsShowAllTimeZone")
    @Expose
    private Object isShowAllTimeZone;
    @SerializedName("SalesforceInTimeline")
    @Expose
    private Object salesforceInTimeline;
    @SerializedName("SalesForceDataImport")
    @Expose
    private Object salesForceDataImport;
    @SerializedName("CreatedOnDate")
    @Expose
    private String createdOnDate;
    @SerializedName("CreatedByUserId")
    @Expose
    private Integer createdByUserId;
    @SerializedName("LastUpdatedOnDate")
    @Expose
    private String lastUpdatedOnDate;
    @SerializedName("LastUpdatedByUserId")
    @Expose
    private Integer lastUpdatedByUserId;
    @SerializedName("ThemeName")
    @Expose
    private Object themeName;
    @SerializedName("CompanyTypeId")
    @Expose
    private Integer companyTypeId;
    @SerializedName("TemplateId")
    @Expose
    private Object templateId;
    @SerializedName("SystemId")
    @Expose
    private Integer systemId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getParentCompanyId() {
        return parentCompanyId;
    }

    public void setParentCompanyId(Integer parentCompanyId) {
        this.parentCompanyId = parentCompanyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFaxnumber() {
        return faxnumber;
    }

    public void setFaxnumber(String faxnumber) {
        this.faxnumber = faxnumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Object getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(Object dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Object getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Object timeZone) {
        this.timeZone = timeZone;
    }

    public Object getIsShowAllTimeZone() {
        return isShowAllTimeZone;
    }

    public void setIsShowAllTimeZone(Object isShowAllTimeZone) {
        this.isShowAllTimeZone = isShowAllTimeZone;
    }

    public Object getSalesforceInTimeline() {
        return salesforceInTimeline;
    }

    public void setSalesforceInTimeline(Object salesforceInTimeline) {
        this.salesforceInTimeline = salesforceInTimeline;
    }

    public Object getSalesForceDataImport() {
        return salesForceDataImport;
    }

    public void setSalesForceDataImport(Object salesForceDataImport) {
        this.salesForceDataImport = salesForceDataImport;
    }

    public String getCreatedOnDate() {
        return createdOnDate;
    }

    public void setCreatedOnDate(String createdOnDate) {
        this.createdOnDate = createdOnDate;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastUpdatedOnDate() {
        return lastUpdatedOnDate;
    }

    public void setLastUpdatedOnDate(String lastUpdatedOnDate) {
        this.lastUpdatedOnDate = lastUpdatedOnDate;
    }

    public Integer getLastUpdatedByUserId() {
        return lastUpdatedByUserId;
    }

    public void setLastUpdatedByUserId(Integer lastUpdatedByUserId) {
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

    public Object getThemeName() {
        return themeName;
    }

    public void setThemeName(Object themeName) {
        this.themeName = themeName;
    }

    public Integer getCompanyTypeId() {
        return companyTypeId;
    }

    public void setCompanyTypeId(Integer companyTypeId) {
        this.companyTypeId = companyTypeId;
    }

    public Object getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Object templateId) {
        this.templateId = templateId;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

}
