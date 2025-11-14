package com.clinic.appointment.controller;

import com.clinic.appointment.dto.appUser.*;
import com.clinic.appointment.dto.profile.ProfileRequest;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.appUser.*;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.service.AppUserSearchService;
import com.clinic.appointment.service.AppUserService;
import com.clinic.appointment.service.excelExport.AppUserExportProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserSearchService searchService;
    private final AppUserExportProcess appUserExportProcess;

    @ModelAttribute("query")
    public AppUserSearchQuery initQuery() {
        AppUserSearchQuery query = new AppUserSearchQuery();
        query.setPageNumber(0);
        query.setPageSize(10);
        query.setSortBy("createdAt");
        query.setSortDirection(SortDirection.DESC);
        query.setFilterList(List.of(
                new AppUserSearchFilter(AppUserSearchField.USERNAME, MatchType.CONTAINS, ""),
                new AppUserSearchFilter(AppUserSearchField.EMAIL, MatchType.CONTAINS, ""),
                new AppUserSearchFilter(AppUserSearchField.STATUS, MatchType.EXACT, "")
        ));
        return query;
    }

    @GetMapping
    public String getAllUsers(@ModelAttribute("query") AppUserSearchQuery query, Model model) {
        Page<AppUser> page = searchService.searchByQuery(query);
        model.addAttribute("users", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "users/listing";
    }

    @PostMapping
    public String searchUsers(@ModelAttribute("query") AppUserSearchQuery query, Model model) {
        Page<AppUser> page = searchService.searchByQuery(query);
        model.addAttribute("users", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalElements", page.getTotalElements());
        return "users/listing";
    }

    @GetMapping("/new")
    public String showCreate(Model model) {
        model.addAttribute("user", new AppUserCreateDTO());
        return "users/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") AppUserCreateDTO dto, BindingResult br) {
        if (br.hasErrors()) return "users/create";
        this.appUserService.create(dto, dto);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        AppUserUpdateDTO user = this.appUserService.findById(id);
        user.setPassword(null);
        model.addAttribute("user", user);
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("user") AppUserUpdateDTO dto, BindingResult br) {
        if (br.hasErrors()) return "users/edit";
        this.appUserService.update(id, dto);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        appUserService.delete(id);
        return "redirect:/users";
    }

    @PostMapping("/export/excel")
    public String exportToExcel(@ModelAttribute("query") AppUserSearchQuery query) {
        appUserExportProcess.generateExportFile(query);
        return "redirect:/users";
    }

    @GetMapping("/view/{id}")
    public String showView(@PathVariable("id") Long id, Model model) {
        AppUserDTO user = this.appUserService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("request",new ProfileRequest());
        return "users/view";
    }

    @PostMapping("/{id}/updatePicture")
    public String updateProfilePicture(@ModelAttribute("request") ProfileRequest profileRequest,@PathVariable("id") Long userId) {
        appUserService.uploadPicture(profileRequest,userId);
        return "redirect:/users/view/" + userId;
    }

}
