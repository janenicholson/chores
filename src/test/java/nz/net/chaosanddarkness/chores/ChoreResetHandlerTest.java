package nz.net.chaosanddarkness.chores;

import static org.hamcrest.Matchers.instanceOf;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.amazonaws.services.lambda.runtime.Context;
import com.asana.errors.NoAuthorizationError;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ChoreResetHandlerTest {

    private static ChoreResetConfiguration input;

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void createInput() throws IOException {
        input = ChoreResetConfiguration.builder().token("NotARealToken").build();
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

        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(instanceOf(NoAuthorizationError.class));

        handler.handleRequest(input, ctx);
    }
}
