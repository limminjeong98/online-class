package org.example.playbackservice.domain.entity;

import com.example.playbackservice.domain.service.PlaybackServiceOuterClass;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Entity
@Table(name = "playback_records")
public class PlaybackRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "start_time", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    @Column(name = "end_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    public PlaybackServiceOuterClass.PlaybackRecord toProto() {
        PlaybackServiceOuterClass.PlaybackRecord.Builder builder = PlaybackServiceOuterClass.PlaybackRecord.newBuilder();

        if (this.recordId != null) {
            builder.setRecordId(this.recordId);
        }
        if (this.userId != null) {
            builder.setUserId(this.userId);
        }
        if (this.fileId != null) {
            builder.setFileId(this.fileId);
        }
        if (this.startTime != null) {
            builder.setStartTime(this.startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }
        if (this.endTime != null) {
            builder.setEndTime(this.endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        }

        return builder.build();
    }
}
