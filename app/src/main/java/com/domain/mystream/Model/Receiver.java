
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Receiver {

    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("UserType")
    @Expose
    private Object userType;
    @SerializedName("AddressId")
    @Expose
    private Object addressId;
    @SerializedName("UserName")
    @Expose
    private Object userName;
    @SerializedName("PassWord")
    @Expose
    private Object passWord;
    @SerializedName("FirstName")
    @Expose
    private Object firstName;
    @SerializedName("MiddleName")
    @Expose
    private Object middleName;
    @SerializedName("LastName")
    @Expose
    private Object lastName;
    @SerializedName("Email")
    @Expose
    private Object email;
    @SerializedName("Gender")
    @Expose
    private Object gender;
    @SerializedName("DateOfBirth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("About")
    @Expose
    private Object about;
    @SerializedName("WebsiteUrl")
    @Expose
    private Object websiteUrl;
    @SerializedName("Ambition")
    @Expose
    private Object ambition;
    @SerializedName("Hobbies")
    @Expose
    private Object hobbies;
    @SerializedName("Position")
    @Expose
    private Object position;
    @SerializedName("Education")
    @Expose
    private Object education;
    @SerializedName("WorkExperience")
    @Expose
    private Object workExperience;
    @SerializedName("WorkDomain")
    @Expose
    private Object workDomain;
    @SerializedName("DateFormate")
    @Expose
    private Object dateFormate;
    @SerializedName("LanguageCode")
    @Expose
    private Object languageCode;
    @SerializedName("Timezone")
    @Expose
    private Object timezone;
    @SerializedName("ShowAllTimezone")
    @Expose
    private Object showAllTimezone;
    @SerializedName("ProfilePic")
    @Expose
    private Object profilePic;
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
    @SerializedName("CompanyId")
    @Expose
    private Object companyId;
    @SerializedName("PhoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("Residence")
    @Expose
    private Object residence;
    @SerializedName("Branch")
    @Expose
    private Object branch;
    @SerializedName("Custom3")
    @Expose
    private Object custom3;
    @SerializedName("Custom4")
    @Expose
    private Object custom4;
    @SerializedName("TemplateColorThemeId")
    @Expose
    private Object templateColorThemeId;
    @SerializedName("Company")
    @Expose
    private Object company;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("ConnectionCount")
    @Expose
    private Integer connectionCount;
    @SerializedName("PhotosCount")
    @Expose
    private Integer photosCount;
    @SerializedName("VideoCount")
    @Expose
    private Integer videoCount;
    @SerializedName("Identifier")
    @Expose
    private Object identifier;
    @SerializedName("Interest")
    @Expose
    private Object interest;
    @SerializedName("CurrentFunction")
    @Expose
    private Object currentFunction;
    @SerializedName("SystemId")
    @Expose
    private Integer systemId;
    @SerializedName("CurrentStatus")
    @Expose
    private Object currentStatus;
    @SerializedName("NotifySms")
    @Expose
    private Boolean notifySms;
    @SerializedName("NotifyEmail")
    @Expose
    private Boolean notifyEmail;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getUserType() {
        return userType;
    }

    public void setUserType(Object userType) {
        this.userType = userType;
    }

    public Object getAddressId() {
        return addressId;
    }

    public void setAddressId(Object addressId) {
        this.addressId = addressId;
    }

    public Object getUserName() {
        return userName;
    }

    public void setUserName(Object userName) {
        this.userName = userName;
    }

    public Object getPassWord() {
        return passWord;
    }

    public void setPassWord(Object passWord) {
        this.passWord = passWord;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getMiddleName() {
        return middleName;
    }

    public void setMiddleName(Object middleName) {
        this.middleName = middleName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getAbout() {
        return about;
    }

    public void setAbout(Object about) {
        this.about = about;
    }

    public Object getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(Object websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Object getAmbition() {
        return ambition;
    }

    public void setAmbition(Object ambition) {
        this.ambition = ambition;
    }

    public Object getHobbies() {
        return hobbies;
    }

    public void setHobbies(Object hobbies) {
        this.hobbies = hobbies;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }

    public Object getEducation() {
        return education;
    }

    public void setEducation(Object education) {
        this.education = education;
    }

    public Object getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Object workExperience) {
        this.workExperience = workExperience;
    }

    public Object getWorkDomain() {
        return workDomain;
    }

    public void setWorkDomain(Object workDomain) {
        this.workDomain = workDomain;
    }

    public Object getDateFormate() {
        return dateFormate;
    }

    public void setDateFormate(Object dateFormate) {
        this.dateFormate = dateFormate;
    }

    public Object getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(Object languageCode) {
        this.languageCode = languageCode;
    }

    public Object getTimezone() {
        return timezone;
    }

    public void setTimezone(Object timezone) {
        this.timezone = timezone;
    }

    public Object getShowAllTimezone() {
        return showAllTimezone;
    }

    public void setShowAllTimezone(Object showAllTimezone) {
        this.showAllTimezone = showAllTimezone;
    }

    public Object getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Object profilePic) {
        this.profilePic = profilePic;
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

    public Object getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Object companyId) {
        this.companyId = companyId;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getResidence() {
        return residence;
    }

    public void setResidence(Object residence) {
        this.residence = residence;
    }

    public Object getBranch() {
        return branch;
    }

    public void setBranch(Object branch) {
        this.branch = branch;
    }

    public Object getCustom3() {
        return custom3;
    }

    public void setCustom3(Object custom3) {
        this.custom3 = custom3;
    }

    public Object getCustom4() {
        return custom4;
    }

    public void setCustom4(Object custom4) {
        this.custom4 = custom4;
    }

    public Object getTemplateColorThemeId() {
        return templateColorThemeId;
    }

    public void setTemplateColorThemeId(Object templateColorThemeId) {
        this.templateColorThemeId = templateColorThemeId;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(Integer connectionCount) {
        this.connectionCount = connectionCount;
    }

    public Integer getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(Integer photosCount) {
        this.photosCount = photosCount;
    }

    public Integer getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(Integer videoCount) {
        this.videoCount = videoCount;
    }

    public Object getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    public Object getInterest() {
        return interest;
    }

    public void setInterest(Object interest) {
        this.interest = interest;
    }

    public Object getCurrentFunction() {
        return currentFunction;
    }

    public void setCurrentFunction(Object currentFunction) {
        this.currentFunction = currentFunction;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public Object getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Object currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Boolean getNotifySms() {
        return notifySms;
    }

    public void setNotifySms(Boolean notifySms) {
        this.notifySms = notifySms;
    }

    public Boolean getNotifyEmail() {
        return notifyEmail;
    }

    public void setNotifyEmail(Boolean notifyEmail) {
        this.notifyEmail = notifyEmail;
    }

}
