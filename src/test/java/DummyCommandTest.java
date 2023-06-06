
import net.imagej.ImageJ;
import org.junit.Assert;
import org.junit.Test;
import org.scijava.command.CommandModule;
import java.util.concurrent.Future;

public class DummyCommandTest {

    public static void main(String... args ) throws Exception {
        // Arrange
        // create the ImageJ application context with all available services
        final ImageJ ij = new ImageJ();

        ij.ui().showUI();
    }
}