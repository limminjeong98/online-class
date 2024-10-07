package org.example.playbackservice.domain.service;

import com.example.playbackservice.domain.service.PlaybackServiceOuterClass;
import io.grpc.stub.StreamObserver;
import org.example.playbackservice.domain.entity.EventLog;
import org.example.playbackservice.domain.entity.PlaybackRecord;
import org.example.playbackservice.domain.repository.EventLogRepository;
import org.example.playbackservice.domain.repository.PlaybackRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaybackServiceTest {

    @InjectMocks
    private PlaybackService playbackService;

    @Mock
    private PlaybackRecordRepository playbackRecordRepository;

    @Mock
    private EventLogRepository eventLogRepository;

    @Mock
    private StreamObserver<PlaybackServiceOuterClass.StartRecordResponse> startRecordResponseObserver;

    @Mock
    private StreamObserver<PlaybackServiceOuterClass.EndRecordResponse> endRecordResponseObserver;

    @Mock
    private StreamObserver<PlaybackServiceOuterClass.LogEventResponse> logEventResponseObserver;

    @Test
    void testStartRecord() {
        // given
        when(playbackRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PlaybackServiceOuterClass.StartRecordRequest startRecordRequest = PlaybackServiceOuterClass.StartRecordRequest.newBuilder()
                .setUserId(1L)
                .setFileId(1L)
                .build();

        // when
        playbackService.startRecord(startRecordRequest, startRecordResponseObserver);

        // then
        verify(playbackRecordRepository).save(any(PlaybackRecord.class));
        verify(startRecordResponseObserver).onNext(any());
        verify(startRecordResponseObserver).onCompleted();
    }

    @Test
    void testEndRecord() {
        // given
        PlaybackRecord playbackRecord = new PlaybackRecord();
        playbackRecord.setRecordId(1L);
        playbackRecord.setUserId(1L);
        playbackRecord.setFileId(1L);
        playbackRecord.setStartTime(LocalDateTime.now());

        when(playbackRecordRepository.findById(any())).thenReturn(Optional.of(playbackRecord));

        PlaybackServiceOuterClass.EndRecordRequest endRecordRequest = PlaybackServiceOuterClass.EndRecordRequest.newBuilder()
                .setRecordId(1L)
                .build();

        // when
        playbackService.endRecord(endRecordRequest, endRecordResponseObserver);


        // then
        verify(playbackRecordRepository).save(any(PlaybackRecord.class));
        verify(endRecordResponseObserver).onNext(any());
        verify(endRecordResponseObserver).onCompleted();
    }

    @Test
    void testLogEvent() {
        // given
        PlaybackRecord playbackRecord = new PlaybackRecord();
        playbackRecord.setRecordId(1L);
        playbackRecord.setUserId(1L);
        playbackRecord.setFileId(1L);
        playbackRecord.setStartTime(LocalDateTime.now());

        when(playbackRecordRepository.findById(anyLong())).thenReturn(Optional.of(playbackRecord));

        EventLog expectedEvent = new EventLog();
        expectedEvent.setPlaybackRecord(playbackRecord);
        expectedEvent.setUserId(1L);
        expectedEvent.setEventType("PLAY");
        expectedEvent.setTimestamp(LocalDateTime.now());

        when(eventLogRepository.save(any(EventLog.class))).thenReturn(expectedEvent);

        PlaybackServiceOuterClass.LogEventRequest logEventRequest = PlaybackServiceOuterClass.LogEventRequest.newBuilder()
                .setEvent(
                        PlaybackServiceOuterClass.EventLog.newBuilder()
                                .setRecordId(1L)
                                .setUserId(1L)
                                .setEventType("PLAY")
                ).build();

        // when
        playbackService.logEvent(logEventRequest, logEventResponseObserver);

        // then
        verify(eventLogRepository).save(any(EventLog.class));
        verify(logEventResponseObserver).onNext(any());
        verify(logEventResponseObserver).onCompleted();
    }


}