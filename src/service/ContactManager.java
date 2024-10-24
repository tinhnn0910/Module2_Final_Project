package service;

import model.Contact;
import util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ContactManager {
    private List<Contact> contacts;
    private static final Scanner scanner = new Scanner(System.in);

    public ContactManager() {
        this.contacts = new ArrayList<>();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n---CHƯƠNG TRÌNH QUẢN LÝ DANH BẠ---");
            System.out.println("Chọn chức năng:");
            System.out.println("1. Xem danh sách");
            System.out.println("2. Thêm mới");
            System.out.println("3. Cập nhật");
            System.out.println("4. Xóa");
            System.out.println("5. Tìm kiếm");
            System.out.println("6. Đọc từ file");
            System.out.println("7. Ghi vào file");
            System.out.println("8. Thoát");

            try {
                int choice = getIntInput("Nhập lựa chọn: ");
                switch (choice) {
                    case 1: displayContacts(); break;
                    case 2: addContact(); break;
                    case 3: updateContact(); break;
                    case 4: deleteContact(); break;
                    case 5: searchContacts(); break;
                    case 6: readFromFile(); break;
                    case 7: writeToFile(); break;
                    case 8: return;
                    default: System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (Exception e) {
                System.out.println("Có lỗi xảy ra: " + e.getMessage());
            }
        }
    }

    private void displayContacts() {
        if (contacts.isEmpty()) {
            System.out.println("Danh bạ trống!");
            return;
        }

        for (int i = 0; i < contacts.size(); i++) {
            if (i > 0 && i % 5 == 0) {
                System.out.println("\nNhấn Enter để xem tiếp...");
                scanner.nextLine();
            }
            displayContact(contacts.get(i));
        }
    }

    private void displayContact(Contact contact) {
        System.out.println("\n----------------------------------------");
        System.out.println("Số điện thoại: " + contact.getPhoneNumber());
        System.out.println("Nhóm: " + (contact.getGroup() != null ? contact.getGroup() : ""));
        System.out.println("Họ tên: " + contact.getName());
        System.out.println("Giới tính: " + (contact.getGender() != null ? contact.getGender() : ""));
        System.out.println("Địa chỉ: " + (contact.getAddress() != null ? contact.getAddress() : ""));
        System.out.println("Ngày sinh: " + (contact.getBirthDate() != null ? contact.getBirthDate() : ""));
        System.out.println("Email: " + (contact.getEmail() != null ? contact.getEmail() : ""));
        System.out.println("----------------------------------------");
    }

    private void addContact() {
        try {
            System.out.println("\nNhập thông tin liên hệ mới:");
            String phone = getInput("Số điện thoại (bắt buộc): ");
            
            // Kiểm tra số điện thoại đã tồn tại
            if (findContactByPhone(phone) != null) {
                System.out.println("Số điện thoại đã tồn tại trong danh bạ!");
                return;
            }

            String group = getInput("Nhóm: ");
            String name = getInput("Họ tên (bắt buộc): ");
            String gender = getInput("Giới tính: ");
            String address = getInput("Địa chỉ: ");
            String birthDate = getInput("Ngày sinh (YYYY-MM-DD): ");
            String email = getInput("Email: ");

            Contact contact = new Contact(phone, group, name, gender, address, birthDate, email);
            contacts.add(contact);
            System.out.println("Thêm liên hệ thành công!");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void updateContact() {
        String phone = getInput("Nhập số điện thoại cần cập nhật: ");
        if (phone.isEmpty()) return;

        Contact contact = findContactByPhone(phone);
        if (contact == null) {
            System.out.println("Không tìm được danh bạ với số điện thoại trên.");
            return;
        }

        try {
            System.out.println("Nhập thông tin mới (Enter để giữ nguyên):");
            
            String group = getInput("Nhóm [" + (contact.getGroup() != null ? contact.getGroup() : "") + "]: ");
            if (!group.isEmpty()) contact.setGroup(group);

            String name = getInput("Họ tên [" + contact.getName() + "]: ");
            if (!name.isEmpty()) contact.setName(name);

            String gender = getInput("Giới tính [" + (contact.getGender() != null ? contact.getGender() : "") + "]: ");
            if (!gender.isEmpty()) contact.setGender(gender);

            String address = getInput("Địa chỉ [" + (contact.getAddress() != null ? contact.getAddress() : "") + "]: ");
            if (!address.isEmpty()) contact.setAddress(address);

            String birthDate = getInput("Ngày sinh (YYYY-MM-DD) [" + 
                (contact.getBirthDate() != null ? contact.getBirthDate() : "") + "]: ");
            if (!birthDate.isEmpty()) contact.setBirthDate(birthDate);

            String email = getInput("Email [" + (contact.getEmail() != null ? contact.getEmail() : "") + "]: ");
            if (!email.isEmpty()) contact.setEmail(email);

            System.out.println("Cập nhật thành công!");
        } catch (IllegalArgumentException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }

    private void deleteContact() {
        String phone = getInput("Nhập số điện thoại cần xóa: ");
        if (phone.isEmpty()) return;

        Contact contact = findContactByPhone(phone);
        if (contact == null) {
            System.out.println("Không tìm được danh bạ với số điện thoại trên.");
            return;
        }

        System.out.print("Bạn có chắc muốn xóa liên hệ này? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (confirm.equals("Y")) {
            contacts.remove(contact);
            System.out.println("Đã xóa liên hệ!");
        }
    }

    private void searchContacts() {
        System.out.println("\n1. Tìm theo số điện thoại");
        System.out.println("2. Tìm theo tên");
        int choice = getIntInput("Chọn cách tìm kiếm: ");

        String searchTerm = getInput("Nhập từ khóa tìm kiếm: ").toLowerCase();
        List<Contact> results = new ArrayList<>();

        if (choice == 1) {
            results = contacts.stream()
                .filter(c -> c.getPhoneNumber().contains(searchTerm))
                .collect(Collectors.toList());
        } else if (choice == 2) {
            results = contacts.stream()
                .filter(c -> c.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        }

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy kết quả nào!");
        } else {
            System.out.println("\nKết quả tìm kiếm:");
            results.forEach(this::displayContact);
        }
    }

    private void readFromFile() {
        System.out.print("Đọc file sẽ xóa dữ liệu hiện tại. Tiếp tục? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (!confirm.equals("Y")) return;

        try (BufferedReader reader = new BufferedReader(
                new FileReader(FileUtils.getContactsFilePath()))) {
            contacts.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    try {
                        Contact contact = new Contact(
                            parts[0], parts[1], parts[2], parts[3],
                            parts[4], parts[5], parts[6]
                        );
                        contacts.add(contact);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Bỏ qua dòng không hợp lệ: " + line);
                    }
                }
            }
            System.out.println("Đọc file thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
        }
    }

    private void writeToFile() {
        System.out.print("Ghi file sẽ xóa dữ liệu cũ. Tiếp tục? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();
        if (!confirm.equals("Y")) return;

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(FileUtils.getContactsFilePath()))) {
            for (Contact contact : contacts) {
                writer.write(contact.toCsvString());
                writer.newLine();
            }
            System.out.println("Ghi file thành công!");
        } catch (IOException e) {
            System.out.println("Lỗi ghi file: " + e.getMessage());
        }
    }

    private Contact findContactByPhone(String phone) {
        return contacts.stream()
            .filter(c -> c.getPhoneNumber().equals(phone))
            .findFirst()
            .orElse(null);
    }

    private String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        }
    }
}
