package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.adapters.api.CombinedComponentResponse;
import com.dreamtracker.app.view.adapters.api.ViewRequest;
import com.dreamtracker.app.view.adapters.api.ViewResponse;

public interface ViewService {
  CombinedComponentResponse getStatsComponent(String viewName);

   ViewResponse createView(ViewRequest viewRequest);

}
