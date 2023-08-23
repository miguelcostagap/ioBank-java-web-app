package io.codeforall.thestudio.ioBank.dto;

import java.util.List;

public class CustomerDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String profilePictureURL;
    private List<AccountDTO> accountDTOS;

    /**
     * Setters & Getters
     */

    public List<AccountDTO> getAccountDTOS() {
        return accountDTOS;
    }

    public void setAccountDTOS(List<AccountDTO> accountDTOS) {
        this.accountDTOS = accountDTOS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }


}
