package nz.net.chaosanddarkness.chores;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import nz.net.chaosanddarkness.chores.asana.AsanaConnection;

public class ChoreResetHandler implements RequestHandler<ChoreResetConfiguration, Void> {

    @Override
    public Void handleRequest(ChoreResetConfiguration config, Context context) {
        new ResetDailyChores(new AsanaConnection(config.getToken())).resetSection(config.getDailySectionId());
        return null;
    }

}
