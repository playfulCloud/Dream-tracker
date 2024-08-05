package com.dreamtracker.app.view.domain.ports;

import com.dreamtracker.app.view.adapters.api.ViewRequest;
import com.dreamtracker.app.view.adapters.api.ViewResponse;
import java.util.UUID;

public interface ViewService {
   ViewResponse createView(ViewRequest viewRequest);

  boolean deleteView(UUID viewUUID);

  ViewResponse getViewByName(String name);

  ViewResponse updatedView(ViewRequest viewRequest);
}
