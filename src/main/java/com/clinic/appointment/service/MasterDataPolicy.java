package com.clinic.appointment.service;

import com.clinic.appointment.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterDataPolicy {

    @Autowired
    private AuthService authService;

    public Long getCurrentUserId() {
        AppUser appUser  = authService.getCurrentUser();
        return appUser.getId();
    }

    public boolean isAdmin(){
        if(this.getActiveRole().equalsIgnoreCase("ROLE_ADMIN")){
            return true;
        }
        return false;
    }

    public String getActiveRole(){
        return authService.getActiveRole();
    }

    public boolean canView(MasterData masterData){
        return isAuthenticated();
    }

    public boolean canDelete(MasterData entity){
        if(entity.isDeleted()){
            return false;
        }
        return true;
    }

    public boolean canDestroy(MasterData entity){
        if(entity.isDeleted()){
            return false;
        }
        if(entity.isAdmin() || entity.isDoctor() || entity.isPatient() || entity.isAppUser()){
            if(!isAdmin()){
                return false;
            }
        }
        return entity.isOwnRecord(this.getCurrentUserId());
    }

    public boolean canUpdate(MasterData entity){
        if(entity.isDeleted()){
            return false;
        }
        return entity.isOwnRecord(this.getCurrentUserId());
    }

    public boolean canCreate(MasterData entity){
        if(entity.isDoctor() && !isAdmin()){
            return false;
        }
        if(entity.isAdmin() && !isAdmin()){
            return false;
        }
        return isAuthenticated();
    }

    public boolean isAuthenticated(){
        Long currentUserId = this.getCurrentUserId();
        return currentUserId != null;
    }

}

