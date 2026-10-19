
package com.example.studentcomplaintsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ComplaintController {

    private final List<Complaint> complaints = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        List<Complaint> visibleComplaints;

        if (isAdmin) {
            visibleComplaints = new ArrayList<>(complaints);
        } else {
            String username = authentication.getName();

            visibleComplaints = complaints.stream()
                    .filter(c -> username.equals(c.getSubmittedBy()))
                    .toList();
        }

        model.addAttribute("complaints", visibleComplaints);
        model.addAttribute("complaint", new Complaint());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("currentUsername", authentication.getName());
        model.addAttribute("totalCount", visibleComplaints.size());
        model.addAttribute("pendingCount", visibleComplaints.stream()
                .filter(c -> "Pending".equals(c.getStatus())).count());
        model.addAttribute("resolvedCount", visibleComplaints.stream()
                .filter(c -> "Resolved".equals(c.getStatus())).count());

        return "index";
    }

    @PostMapping("/complaints")
    public String submit(
            @ModelAttribute Complaint complaint,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (complaint.getCategory() == null
                || complaint.getCategory().isBlank()
                || complaint.getDescription() == null
                || complaint.getDescription().isBlank()) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Please fill in every field.");
            return "redirect:/";
        }

        String username = authentication.getName();

        // Never trust a student name or submitter sent by the browser.
        complaint.setSubmittedBy(username);
        complaint.setStudentName(username);
        complaint.setId(nextId.getAndIncrement());
        complaint.setCategory(complaint.getCategory().trim());
        complaint.setDescription(complaint.getDescription().trim());
        complaint.setStatus("Pending");

        synchronized (complaints) {
            complaints.add(0, complaint);
        }

        redirectAttributes.addFlashAttribute(
                "successMessage", "Complaint submitted successfully.");

        return "redirect:/";
    }

    @PostMapping("/complaints/{id}/status")
    public String updateStatus(
            @PathVariable long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (!List.of("Pending", "In Progress", "Resolved")
                .contains(status)) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Invalid status selected.");
            return "redirect:/";
        }

        boolean updated = false;

        synchronized (complaints) {
            for (Complaint complaint : complaints) {
                if (complaint.getId() == id) {
                    complaint.setStatus(status);
                    updated = true;
                    break;
                }
            }
        }

        if (updated) {
            redirectAttributes.addFlashAttribute(
                    "successMessage", "Complaint status updated.");
        } else {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Complaint not found.");
        }

        return "redirect:/";
    }
}
