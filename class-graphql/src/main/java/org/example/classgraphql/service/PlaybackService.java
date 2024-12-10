package org.example.classgraphql.service;

import com.example.playbackservice.domain.service.PlaybackServiceGrpc;
import com.example.playbackservice.domain.service.PlaybackServiceOuterClass;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.classgraphql.model.EventLog;
import org.example.classgraphql.model.PlaybackRecord;
import org.springframework.stereotype.Service;

@Service
public class PlaybackService {

    @GrpcClient("playback-service")
    private PlaybackServiceGrpc.PlaybackServiceBlockingStub playbackServiceStub;

    public PlaybackRecord startRecord(Long userId, Long fileId) {
        PlaybackRecord record = new PlaybackRecord();
        record.setUserId(userId);
        record.setFileId(fileId);

        // graphql model -> protobuf
        PlaybackServiceOuterClass.StartRecordRequest request = PlaybackServiceOuterClass.StartRecordRequest.newBuilder()
                .setUserId(userId)
                .setFileId(fileId)
                .build();

        // protobuf -> graphql
        PlaybackServiceOuterClass.StartRecordResponse response = playbackServiceStub.startRecord(request);
        return PlaybackRecord.fromProto(response.getRecord());
    }

    public PlaybackRecord endRecord(Long recordId) {
        PlaybackServiceOuterClass.EndRecordRequest request = PlaybackServiceOuterClass.EndRecordRequest.newBuilder()
                .setRecordId(recordId)
                .build();

        PlaybackServiceOuterClass.EndRecordResponse response = playbackServiceStub.endRecord(request);
        return PlaybackRecord.fromProto(response.getRecord());
    }

    public EventLog logEvent(EventLog eventLog) {
        PlaybackServiceOuterClass.LogEventRequest request = PlaybackServiceOuterClass.LogEventRequest.newBuilder()
                .setEvent(EventLog.toProto(eventLog))
                .build();

        PlaybackServiceOuterClass.LogEventResponse response = playbackServiceStub.logEvent(request);

        return EventLog.fromProto(response.getEvent());
    }
}
