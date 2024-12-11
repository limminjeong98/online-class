package org.example.classgraphql.model;

import com.example.playbackservice.domain.service.PlaybackServiceOuterClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaybackRecord implements Serializable {
    private Long recordId;
    private Long userId;
    private Long fileId;
    private String startTime;
    private String endTime;

    public static PlaybackRecord fromProto(PlaybackServiceOuterClass.PlaybackRecord proto) {
        PlaybackRecord record = new PlaybackRecord();
        record.setRecordId(proto.getRecordId());
        record.setUserId(proto.getUserId());
        record.setFileId(proto.getFileId());
        record.setStartTime(Instant.ofEpochMilli(proto.getStartTime()).toString()); // Convert to ISO 8601
        record.setEndTime(Instant.ofEpochMilli(proto.getEndTime()).toString());
        return record;
    }
}
