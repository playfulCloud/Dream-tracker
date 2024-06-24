package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.infrastructure.response.Page;
import com.dreamtracker.app.view.adapters.api.StatsComponentResponse;
import com.dreamtracker.app.view.adapters.api.ViewRequest;
import com.dreamtracker.app.view.adapters.api.ViewResponse;

public interface ViewService {
   Page<StatsComponentResponse> getStatsComponent(String viewName);
   ViewResponse createView(ViewRequest viewRequest);

}
