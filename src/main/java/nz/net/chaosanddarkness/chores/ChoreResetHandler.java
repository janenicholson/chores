package nz.net.chaosanddarkness.chores;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import nz.net.chaosanddarkness.chores.asana.AsanaConnection;

public class ChoreResetHandler implements RequestHandler<String, Void> {

    @Override
    public Void handleRequest(String token, Context context) {
        new ResetDailyChores(new AsanaConnection(token)).cheatReset();
        return null;
    }

}
