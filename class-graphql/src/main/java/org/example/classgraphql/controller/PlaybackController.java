package org.example.classgraphql.controller;

import org.example.classgraphql.model.EventLog;
import org.example.classgraphql.model.PlaybackRecord;
import org.example.classgraphql.service.PlaybackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PlaybackController {

    private PlaybackService playbackService;

    @Autowired
    public PlaybackController(PlaybackService playbackService) {
        this.playbackService = playbackService;
    }

    @MutationMapping
    public PlaybackRecord startRecord(@Argument Long userId, @Argument Long fileId) {
        // throw new NotImplementedException();
        return playbackService.startRecord(userId, fileId);
    }

    @MutationMapping
    public PlaybackRecord endRecord(@Argument Long userId, @Argument Long recordId) {
        // throw new NotImplementedException();
        return playbackService.endRecord(recordId);
    }

    @MutationMapping
    public EventLog logEvent(@Argument Long recordId, @Argument Long userId, @Argument String eventType, @Argument String timestamp) {
        // throw new NotImplementedException();
        EventLog eventLog = new EventLog(null, recordId, userId, eventType, timestamp);
        return playbackService.logEvent(eventLog);
    }
}
