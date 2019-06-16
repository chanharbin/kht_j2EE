package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.service.model.OrganizationModel;

public interface OrganizationService {

    public Result increaseOrganization(OrganizationModel organizationModel);

    public Result decreaseOrganization(String organizationId);

    public Result getOrganization(int pageNum);

    public Result updateOrganization(OrganizationModel organizationModel);

}
