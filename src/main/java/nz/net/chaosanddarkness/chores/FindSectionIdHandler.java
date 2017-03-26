package nz.net.chaosanddarkness.chores;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import nz.net.chaosanddarkness.chores.asana.AsanaReader;

public class FindSectionIdHandler implements RequestHandler<SectionIdFinderRequest, String> {

    @Override
    public String handleRequest(SectionIdFinderRequest config, Context context) {
        AsanaReader asanaReader = new AsanaReader(config.getToken());
        return asanaReader.getSectionId(config.getWorkspaceName(), config.getProjectName(), config.getSectionName());
    }

}
