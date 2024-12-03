package org.example.classgraphql.resolver;

import org.example.classgraphql.model.CourseSession;
import org.example.classgraphql.model.CourseSessionFile;
import org.example.classgraphql.service.DummyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CourseSessionDataResolver {

    private final DummyFileService fileService;

    @Autowired
    public CourseSessionDataResolver(DummyFileService fileService) {
        this.fileService = fileService;
    }

    @SchemaMapping(typeName = "CourseSession", field = "files")
    public List<CourseSessionFile> getFiles(CourseSession courseSession) {
        return fileService.findFilesBySessionId(courseSession.getId());
    }


}
