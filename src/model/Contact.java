package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import util.ValidationUtils;

public class Contact {
    private String phoneNumber;
    private String group;
    private String name;
    private String gender;
    private String address;
    private LocalDate birthDate;
    private String email;

    public Contact(String phoneNumber, String group, String name, String gender,
                   String address, String birthDate, String email) throws IllegalArgumentException {
        validateData(phoneNumber, name, email, birthDate);
        this.phoneNumber = phoneNumber;
        this.group = group;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.birthDate = birthDate != null && !birthDate.isEmpty() ?
                LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE) : null;
        this.email = email;
    }

    private void validateData(String phone, String name, String email, String birthDate) {
        if (phone == null || name == null || phone.trim().isEmpty() || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại và họ tên là bắt buộc!");
        }

        if (!ValidationUtils.isValidPhoneNumber(phone)) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ!");
        }

        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }

        if (!ValidationUtils.isValidDate(birthDate)) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ (định dạng: YYYY-MM-DD)!");
        }
    }

    // Getters
    public String getPhoneNumber() { return phoneNumber; }
    public String getGroup() { return group; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getAddress() { return address; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getEmail() { return email; }

    // Setters
    public void setGroup(String group) { this.group = group; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống!");
        }
        this.name = name;
    }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setBirthDate(String birthDate) {
        if (!ValidationUtils.isValidDate(birthDate)) {
            throw new IllegalArgumentException("Ngày sinh không hợp lệ!");
        }
        this.birthDate = birthDate != null && !birthDate.isEmpty() ?
                LocalDate.parse(birthDate, DateTimeFormatter.ISO_DATE) : null;
    }
    public void setEmail(String email) {
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }
        this.email = email;
    }

    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                phoneNumber,
                group != null ? group : "",
                name,
                gender != null ? gender : "",
                address != null ? address : "",
                birthDate != null ? birthDate.format(DateTimeFormatter.ISO_DATE) : "",
                email != null ? email : "");
    }

    @Override
    public String toString() {
        return String.format("Số ĐT: %s | Tên: %s | Nhóm: %s",
                phoneNumber, name, group != null ? group : "");
    }
}