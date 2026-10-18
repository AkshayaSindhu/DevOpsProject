package com.example.studentcomplaintsystem;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ComplaintController {
    private final List<Complaint> complaints = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public ComplaintController() {
        complaints.add(new Complaint(nextId.getAndIncrement(), "Asha", "Hostel", "Water supply issue in the hostel.", "Pending"));
        complaints.add(new Complaint(nextId.getAndIncrement(), "Rahul", "Classroom", "Projector is not working.", "In Progress"));
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("complaints", complaints);
        model.addAttribute("complaint", new Complaint());
        model.addAttribute("totalCount", complaints.size());
        model.addAttribute("pendingCount", complaints.stream().filter(c -> "Pending".equals(c.getStatus())).count());
        model.addAttribute("resolvedCount", complaints.stream().filter(c -> "Resolved".equals(c.getStatus())).count());
        return "index";
    }

    @PostMapping("/complaints")
    public String submit(@ModelAttribute Complaint complaint, RedirectAttributes redirectAttributes) {
        if (complaint.getStudentName() == null || complaint.getStudentName().isBlank()
                || complaint.getCategory() == null || complaint.getCategory().isBlank()
                || complaint.getDescription() == null || complaint.getDescription().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fill in every field.");
            return "redirect:/";
        }
        complaint.setId(nextId.getAndIncrement());
        complaint.setStudentName(complaint.getStudentName().trim());
        complaint.setDescription(complaint.getDescription().trim());
        complaint.setStatus("Pending");
        complaints.add(0, complaint);
        redirectAttributes.addFlashAttribute("successMessage", "Your complaint was submitted successfully.");
        return "redirect:/";
    }

    @PostMapping("/complaints/{id}/status")
    public String updateStatus(@PathVariable long id, @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        if (!List.of("Pending", "In Progress", "Resolved").contains(status)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid status selected.");
            return "redirect:/";
        }
        complaints.stream().filter(c -> c.getId() == id).findFirst()
                .ifPresent(c -> c.setStatus(status));
        redirectAttributes.addFlashAttribute("successMessage", "Complaint status updated.");
        return "redirect:/";
    }
}
