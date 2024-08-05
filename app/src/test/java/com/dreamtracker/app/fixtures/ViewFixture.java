package com.dreamtracker.app.fixtures;

import com.dreamtracker.app.view.adapters.api.ViewRequest;
import com.dreamtracker.app.view.adapters.api.ViewResponse;
import com.dreamtracker.app.view.domain.model.View;

import java.util.UUID;

public interface ViewFixture {


    default View.ViewBuilder getViewBuilder() {
        return View.builder()
                .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                .name("test")
                .description("test")
                .habits(true)
                .stats(true)
                .goals(true);
    }

    default ViewRequest.ViewRequestBuilder getViewRequestBuilder() {
        return ViewRequest.builder()
                .name("test")
                .description("test")
                .habits(true)
                .stats(true)
                .goals(true);
    }

    default ViewResponse.ViewResponseBuilder getViewResponseBuilder() {
        return ViewResponse.builder()
                .id(UUID.fromString("8fbb366d-64bb-4e2a-8527-93085885270e"))
                .name("test")
                .description("test")
                .habits(true)
                .stats(true)
                .goals(true);
    }


}


