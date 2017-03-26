package nz.net.chaosanddarkness.chores;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import nz.net.chaosanddarkness.chores.asana.AsanaReader;
import nz.net.chaosanddarkness.chores.asana.AsanaUpdater;

public class ChoreResetHandler implements RequestHandler<ChoreResetConfiguration, Void> {

    @Override
    public Void handleRequest(ChoreResetConfiguration config, Context context) {
        try {
            AsanaReader asanaReader = new AsanaReader(config.getToken());
            AsanaUpdater asanaUpdater = new AsanaUpdater(config.getToken());
            new ResetDailyChoresTask(asanaReader, asanaUpdater).resetSection(config.getDailySectionId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
