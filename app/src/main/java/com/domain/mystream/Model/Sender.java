
package com.domain.mystream.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sender {

    @SerializedName("UserId")
    @Expose
    private Integer userId;
    @SerializedName("UserType")
    @Expose
    private Integer userType;
    @SerializedName("AddressId")
    @Expose
    private Integer addressId;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("PassWord")
    @Expose
    private String passWord;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("MiddleName")
    @Expose
    private String middleName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("DateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("About")
    @Expose
    private String about;
    @SerializedName("WebsiteUrl")
    @Expose
    private String websiteUrl;
    @SerializedName("Ambition")
    @Expose
    private String ambition;
    @SerializedName("Hobbies")
    @Expose
    private String hobbies;
    @SerializedName("Position")
    @Expose
    private String position;
    @SerializedName("Education")
    @Expose
    private String education;
    @SerializedName("WorkExperience")
    @Expose
    private String workExperience;
    @SerializedName("WorkDomain")
    @Expose
    private String workDomain;
    @SerializedName("DateFormate")
    @Expose
    private String dateFormate;
    @SerializedName("LanguageCode")
    @Expose
    private String languageCode;
    @SerializedName("Timezone")
    @Expose
    private String timezone;
    @SerializedName("ShowAllTimezone")
    @Expose
    private Boolean showAllTimezone;
    @SerializedName("ProfilePic")
    @Expose
    private String profilePic;
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
    private Integer companyId;
    @SerializedName("PhoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("Residence")
    @Expose
    private String residence;
    @SerializedName("Branch")
    @Expose
    private String branch;
    @SerializedName("Custom3")
    @Expose
    private String custom3;
    @SerializedName("Custom4")
    @Expose
    private String custom4;
    @SerializedName("TemplateColorThemeId")
    @Expose
    private Object templateColorThemeId;
    @SerializedName("Company")
    @Expose
    private Company_ company;
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
    private String identifier;
    @SerializedName("Interest")
    @Expose
    private String interest;
    @SerializedName("CurrentFunction")
    @Expose
    private String currentFunction;
    @SerializedName("SystemId")
    @Expose
    private Integer systemId;
    @SerializedName("CurrentStatus")
    @Expose
    private String currentStatus;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getAmbition() {
        return ambition;
    }

    public void setAmbition(String ambition) {
        this.ambition = ambition;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public String getWorkDomain() {
        return workDomain;
    }

    public void setWorkDomain(String workDomain) {
        this.workDomain = workDomain;
    }

    public String getDateFormate() {
        return dateFormate;
    }

    public void setDateFormate(String dateFormate) {
        this.dateFormate = dateFormate;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getShowAllTimezone() {
        return showAllTimezone;
    }

    public void setShowAllTimezone(Boolean showAllTimezone) {
        this.showAllTimezone = showAllTimezone;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCustom3() {
        return custom3;
    }

    public void setCustom3(String custom3) {
        this.custom3 = custom3;
    }

    public String getCustom4() {
        return custom4;
    }

    public void setCustom4(String custom4) {
        this.custom4 = custom4;
    }

    public Object getTemplateColorThemeId() {
        return templateColorThemeId;
    }

    public void setTemplateColorThemeId(Object templateColorThemeId) {
        this.templateColorThemeId = templateColorThemeId;
    }

    public Company_ getCompany() {
        return company;
    }

    public void setCompany(Company_ company) {
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getCurrentFunction() {
        return currentFunction;
    }

    public void setCurrentFunction(String currentFunction) {
        this.currentFunction = currentFunction;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
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
