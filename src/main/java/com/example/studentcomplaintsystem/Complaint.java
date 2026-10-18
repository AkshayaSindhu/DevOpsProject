package com.example.studentcomplaintsystem;

public class Complaint {
    private long id;
    private String studentName;
    private String category;
    private String description;
    private String status;

    public Complaint() {}

    public Complaint(long id, String studentName, String category, String description, String status) {
        this.id = id;
        this.studentName = studentName;
        this.category = category;
        this.description = description;
        this.status = status;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
