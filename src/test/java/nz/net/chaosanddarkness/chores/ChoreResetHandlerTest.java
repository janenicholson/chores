package nz.net.chaosanddarkness.chores;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ChoreResetHandlerTest {

    private static String input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = "NotARealToken";
    }

    private Context createContext() {
        TestContext ctx = new TestContext();

        // TODO: customize your context here if needed.
        ctx.setFunctionName("Your Function Name");

        return ctx;
    }

    @Test
    public void testLambdaFunctionHandler() {
        ChoreResetHandler handler = new ChoreResetHandler();
        Context ctx = createContext();

        handler.handleRequest(input, ctx);
    }
}
