package com.clinic.appointment.controller;

import com.clinic.appointment.annotation.ActiveRole;
import com.clinic.appointment.annotation.ActiveUser;
import com.clinic.appointment.dto.appUser.*;
import com.clinic.appointment.dto.searchFilter.MatchType;
import com.clinic.appointment.dto.searchFilter.SortDirection;
import com.clinic.appointment.dto.searchFilter.appUser.*;
import com.clinic.appointment.service.AppUserSearchService;
import com.clinic.appointment.service.AppUserService;
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
    public String list(@ModelAttribute("query") AppUserSearchQuery query, Model model) {
        Page<AppUserDTO> page = searchService.searchUsers(query);
        model.addAttribute("users", page.getContent());
        model.addAttribute("totalPages", page.getTotalPages());
        return "users/listing";
    }

    @GetMapping("/new")
    @ActiveRole({"ADMIN"})
    public String showCreate(Model model) {
        model.addAttribute("user", new AppUserCreateDTO());
        return "users/create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("user") AppUserCreateDTO dto,
                         BindingResult result,
                         @ActiveUser AppUser actor,
                         Model model) {

        if (result.hasErrors()) return "users/create";

        appUserService.create(dto, actor);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("user", appUserService.findById(id));
        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("user") AppUserUpdateDTO dto,
                         BindingResult result,
                         @ActiveUser AppUser actor,
                         Model model) {

        if (result.hasErrors()) return "users/edit";

        appUserService.update(id, dto, actor);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    @ActiveRole({"ADMIN"})
    public String delete(@PathVariable Long id, @ActiveUser AppUser actor) {
        appUserService.delete(id, actor);
        return "redirect:/users";
    }

    @PostMapping("/export/excel")
    public String exportToExcel(@ModelAttribute("query") AppUserSearchQuery query) {
        appUserExportProcess.generateExportFile(query);
        return "redirect:/users";
    }

}
