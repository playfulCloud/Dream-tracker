package com.dreamtracker.app.service.impl;

import com.dreamtracker.app.entity.HabitTrack;
import com.dreamtracker.app.fixtures.HabitFixture;
import com.dreamtracker.app.fixtures.HabitTrackFixture;
import com.dreamtracker.app.repository.HabitRepository;
import com.dreamtracker.app.repository.HabitTrackRepository;
import com.dreamtracker.app.response.HabitTrackResponse;
import com.dreamtracker.app.response.Page;
import com.dreamtracker.app.security.CurrentUserProvider;
import com.dreamtracker.app.service.HabitService;
import com.dreamtracker.app.service.HabitTrackService;
import com.dreamtracker.app.utils.HabitStatus;
import com.dreamtracker.app.utils.HabitTrackStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HabitTrackServiceImplTest implements HabitFixture, HabitTrackFixture {


    private HabitTrackService habitTrackService ;
    private final HabitTrackRepository habitTrackRepository = Mockito.mock(HabitTrackRepository.class);
    private final HabitRepository habitRepository = Mockito.mock(HabitRepository.class);
    private final CurrentUserProvider currentUserProvider = new MockCurrentUserProviderImpl();


    @BeforeEach
    void setUp(){
       habitTrackService = new HabitTrackServiceImpl(habitTrackRepository, habitRepository);
    }

    @Test
    void getAllTracksOfHabitPositiveTestCase() {
        //given

        var dateForTracks = ZonedDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        var sampleHabitTrack = getSampleHabitTrack(sampleHabit.getId(),dateForTracks).build();
        var sampleHabitTrackResponse = getSampleHabitTrackResponse(dateForTracks).build();
        var listOfTracks = List.of(sampleHabitTrack
        );
        var expectedPageItems = List.of(
               sampleHabitTrackResponse
        );
        var expectedPage = new Page<HabitTrackResponse>(expectedPageItems);
        when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.of(sampleHabit));
        when(habitTrackRepository.findByHabitUUID(sampleHabit.getId())).thenReturn(listOfTracks);
        //when
        var actualPageResponse =  habitTrackService.getAllTracksOfHabit(sampleHabit.getId());
        //then
        assertThat(actualPageResponse).isEqualTo(expectedPage);
      }

    @Test
    void getAllTracksOfHabitEmptyPage() {
        //given
        var sampleHabit = getSampleHabitBuilder(currentUserProvider.getCurrentUser()).build();
        when(habitRepository.findById(sampleHabit.getId())).thenReturn(Optional.empty());
        //when
        var actualPageResponse =  habitTrackService.getAllTracksOfHabit(sampleHabit.getId());
        //then
        assertThat(actualPageResponse.getItems().size()).isEqualTo(0);
    }


    @Test
    void trackTheHabit() {

      }
}